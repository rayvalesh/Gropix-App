package com.coagere.gropix.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiDetector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.coagere.gropix.R;
import com.tc.utils.utils.helpers.HelperTime;
import com.tc.utils.utils.helpers.StoragePath;
import com.tc.utils.utils.utility.UtilityCheckPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import tk.jamunx.ui.camera.customs.CameraSource;
import tk.jamunx.ui.camera.customs.CameraSourcePreview;
import tk.jamunx.ui.camera.customs.FaceGraphic;
import tk.jamunx.ui.camera.customs.ScaleListener;
import tk.jamunx.ui.camera.ui.Exis;
import tk.jamunx.ui.camera.ui.GraphicOverlay;
import tk.jamunx.ui.camera.utils.CheckOs;
import tk.jamunx.ui.camera.utils.HelperActionBar;
import tk.jamunx.ui.camera.utils.UtilityClass;

import static tk.jamunx.ui.camera.utils.InterfaceUtils.CAMERA_ACTION_REQUEST_GALLERY;
import static tk.jamunx.ui.camera.utils.InterfaceUtils.CAMERA_INTENT_COME_FROM;
import static tk.jamunx.ui.camera.utils.InterfaceUtils.CAMERA_RESULT_IMAGE_PATH;

public final class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private CameraSource cameraSource = null;
    private CameraSourcePreview mPreview;
    private GraphicOverlay graphicOverlay;

    private int cameraRotationId;
    private File iFile;
    private ImageView imageView;
    private AppCompatImageView imageViewRotation, imageViewFlash;
    private TextView imageViewCamera;
    private int comeFrom;
    private boolean isImageVisible;
    private boolean isImageCropped = false;
    private ScaleGestureDetector scaleGestureDetector;
    private UtilityClass utilityClass;
    private int rtt;
    private Bitmap bitmap;
    public static int FILE_MAX_SIZE = 1024 * 1024 * 12;
    private MultiDetector multiDetector;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        checkAndStart();
    }

    private void checkAndStart() {
        if (UtilityCheckPermission.INSTANCE.checkPermission(this, UtilityCheckPermission.MY_PERMISSIONS_REQUEST_CAMERA)) {
            Intent intent = getIntent();
            utilityClass = new UtilityClass();
            setContentView(R.layout.activity_custom_camera);
            comeFrom = intent.getIntExtra(CAMERA_INTENT_COME_FROM, 0);
            setToolbar();
            initializeView();
            setVisibility();
            String imagePath = intent.getStringExtra(CAMERA_RESULT_IMAGE_PATH);
            if (imagePath != null) {
                iFile = new File(imagePath);
            }
            cameraRotationId = CameraSource.CAMERA_FACING_BACK;
            mPreview = findViewById(R.id.id_preview);
            graphicOverlay = findViewById(R.id.id_faceOverlay);
            scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener(cameraSource));
            FaceDetector fD = new FaceDetector.Builder(getApplicationContext())
                    .setTrackingEnabled(false)
                    .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                    .build();
            fD.setProcessor(
                    new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory()).build());
            multiDetector = new MultiDetector.Builder()
                    .add(fD)
                    .build();
            if (!multiDetector.isOperational()) {
                IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;
                if (hasLowStorage) {
                    Toast.makeText(this, R.string.library_string_toast_low_storage_error, Toast.LENGTH_LONG).show();
                }
                finish();
            }
            int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                    getApplicationContext());
            if (code != ConnectionResult.SUCCESS) {
                Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, 9001);
                dlg.show();
            } else {
                createCameraSource();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (CheckOs.permissionRequestResult(grantResults)) {
            checkAndStart();
        } else {
            finish();
        }
    }

    private void setVisibility() {
        if (utilityClass.isFrontCameraId(this) == -1) {
            imageViewRotation.setVisibility(View.GONE);
        } else {
            imageViewRotation.setVisibility(View.VISIBLE);
        }
        if (utilityClass.isFlashHardware(this)) {
            imageViewFlash.setVisibility(View.VISIBLE);
        } else {
            imageViewFlash.setVisibility(View.GONE);
        }
    }

    private void initializeView() {
        imageViewCamera = findViewById(R.id.id_image_camera);
        imageViewCamera.setOnClickListener(this::onClickCamera);
        imageView = findViewById(R.id.id_image);
        imageViewRotation = findViewById(R.id.id_image_rotation);
        imageViewFlash = findViewById(R.id.id_image_flash);
        //   textViewRotation = findViewById(R.id.id_text_rotate);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.id_app_bar);
        setSupportActionBar(toolbar);
        HelperActionBar.getActionBar(getSupportActionBar(), R.string.library_string_activity_name_camera, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (imageView.getVisibility() == View.VISIBLE) {
                confirmationDialog(getString(R.string.library_string_message_alert_are_you_sure_go_back));
            } else {
                closeEverything();
                finish();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (imageView != null && imageView.getVisibility() == View.VISIBLE) {
            return super.onTouchEvent(e);
        } else {
            if (scaleGestureDetector != null) {
                boolean b = scaleGestureDetector.onTouchEvent(e);
                return b || super.onTouchEvent(e);
            } else {
                return super.onTouchEvent(e);
            }
        }

    }

    private void createCameraSource() {
        if (cameraSource != null) {
            cameraSource.stop();
            cameraSource.release();
        }
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int h = metrics.heightPixels;
        int w = metrics.widthPixels;
        if (h > w) {
            h = metrics.widthPixels;
            w = metrics.heightPixels;
        }
        cameraSource = new CameraSource.Builder(getApplicationContext(), multiDetector)
                .setRequestedPreviewSize(w, h)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15.0f)
                .setFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                .build();
        startCameraSource();
    }

    private void startCameraSource() {
        if (cameraSource != null && mPreview != null) {
            try {
                mPreview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closePreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeSource();
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                Uri selectedImage = data.getData();
                final InputStream imageStream;
                if (selectedImage != null) {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    iFile = StoragePath.INSTANCE.createSentImageFile(HelperTime.get().getTimeStamp() + ".jpg");
                    saveImageIntoMemo(bitmap, iFile);
                    if (iFile.length() < FILE_MAX_SIZE) {
                        imageView.setImageBitmap(bitmap);
                        setImageView();
                    } else {
                        Toast.makeText(this, getString(R.string.library_string_snackbar_file_size), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Method used for saving image into Storage
     *
     * @param bitmap Bitmap Object
     * @param file   File which gonna save
     * @return Status of work
     */
    public boolean saveImageIntoMemo(Bitmap bitmap, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTION_REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                onSelectFromGalleryResult(data);
            }
        }
    }

    private void setImageView() {
        findViewById(R.id.id_image_right).setVisibility(View.VISIBLE);
        findViewById(R.id.id_image_cancel).setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        imageViewCamera.setVisibility(View.GONE);
        imageViewFlash.setVisibility(View.GONE);
        imageViewRotation.setVisibility(View.GONE);
    }


    public void onClickFlash(View view) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            if (cameraSource != null) {
                String mode = null;
                if (cameraSource.getFlashMode() == null) {
                    cameraSource.setFlashModeNew(Camera.Parameters.FLASH_MODE_OFF);
                } else if (cameraSource.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {
                    imageViewFlash.setImageResource(R.drawable.library_icon_vd_flash_on);
                    mode = Camera.Parameters.FLASH_MODE_TORCH;
                } else if (cameraSource.getFlashMode().equals(Camera.Parameters.FLASH_MODE_ON)) {
                    imageViewFlash.setImageResource(R.drawable.library_icon_vd_flash_auto);
                    mode = Camera.Parameters.FLASH_MODE_AUTO;
                } else {
                    imageViewFlash.setImageResource(R.drawable.library_icon_vd_flash_off);
                    mode = Camera.Parameters.FLASH_MODE_OFF;
                }
                cameraSource.setFlashModeNew(mode);
            }
        } else {
            Toast.makeText(CameraActivity.this, "Flash not Available", Toast.LENGTH_SHORT).show();
            imageViewFlash.setVisibility(View.GONE);
        }
    }

    public void onClickRotation(View view) {
        if (cameraSource.getCameraFacing() == CameraSource.CAMERA_FACING_BACK) {
            cameraRotationId = CameraSource.CAMERA_FACING_FRONT;
            imageViewRotation.setImageResource(R.drawable.library_icon_vd_camera_rear);
        } else {
            imageViewRotation.setImageResource(R.drawable.library_icon_vd_camera_front);
            cameraRotationId = CameraSource.CAMERA_FACING_BACK;
        }
        createCameraSource();
    }

    public void onClickCancel(View view) {
        confirmationDialog(getString(R.string.library_string_message_alert_are_you_sure_discard_changes));
    }

    private void confirmationDialog(String message) {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(message)
                .setPositiveButton(getString(R.string.library_string_button_name_yes_want), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isImageCropped) {
                        }
                        isImageVisible = false;
                        deleteFile(iFile);
                        findViewById(R.id.id_image_cancel).setVisibility(View.GONE);
                        findViewById(R.id.id_image_right).setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        imageViewCamera.setVisibility(View.VISIBLE);
                        initializeView();
                        imageView.setImageDrawable(null);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.library_string_button_name_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void deleteFile(File iFile) {
        if (iFile != null) {
            if (iFile.exists()) {
                iFile.delete();
            }
        }
    }

    public void onClickSave(View view) {
        imageView.setImageDrawable(null);
        closePreview();
        closeSource();
        setResult(RESULT_OK, new Intent().putExtra(CAMERA_RESULT_IMAGE_PATH, iFile.getAbsolutePath()));
        finish();
    }

    private void closeSource() {
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
    }

    private void closePreview() {
        if (mPreview != null) {
            mPreview.stop();
            mPreview = null;
        }
    }

    private void onClickCamera(View view) {
        cameraSource.takePicture(null, bytes -> {
            try {
                Bitmap loadedImage = null;
                rtt = Exis.getOrientation(bytes);
                loadedImage = BitmapFactory.decodeByteArray(bytes, 0,
                        bytes.length);
                Matrix rotateMatrix = new Matrix();
                rotateMatrix.postRotate(rtt);
                bitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                        loadedImage.getWidth(), loadedImage.getHeight(),
                        rotateMatrix, false);
                iFile = StoragePath.INSTANCE.createTempFile(CameraActivity.this);
                ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                FileOutputStream fout = new FileOutputStream(iFile);
                fout.write(ostream.toByteArray());
                fout.close();

                imageView.setImageBitmap(bitmap);
                isImageVisible = true;
                setImageView();
            } catch (Exception ignored) {
                isImageVisible = false;
            }
        });

    }

    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(graphicOverlay);
        }
    }

    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
        }

        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }

        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateItem(face);
        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
        }

        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }

    private void startGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
        startActivityForResult(chooser, CAMERA_ACTION_REQUEST_GALLERY);
    }

    private void closeEverything() {
        closePreview();
        closeSource();
    }

    @Override
    public void onBackPressed() {
        closeEverything();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        startGallery();
    }
}
