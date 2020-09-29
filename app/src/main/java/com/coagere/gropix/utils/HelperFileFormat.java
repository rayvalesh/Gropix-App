package com.coagere.gropix.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.coagere.gropix.R;
import com.coagere.gropix.prefs.UserStorage;
import com.tc.utils.utils.helpers.StoragePath;
import com.tc.utils.variables.abstracts.OnEventOccurListener;
import com.tc.utils.variables.interfaces.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tk.jamun.ui.snacks.L;


/**
 * Class provide every required file related operations for your ease
 */
public class HelperFileFormat {
    private String CONSTANT_DATE = "yyyy-MM-dd HH:mm:ss";
    private static HelperFileFormat helperFileFormat;
    private String storageProvider;
    private ArrayList<String> fileType = new ArrayList<>();
    private ArrayList<String> imageType = new ArrayList<>();
    private ArrayList<String> soundType = new ArrayList<>();

    /**
     * Method Generate one time object for all over the session use
     *
     * @return
     */
    public static HelperFileFormat getInstance() {
        if (helperFileFormat == null) {
            helperFileFormat = new HelperFileFormat();
        }
        return helperFileFormat;
    }

    /**
     * Method provide you your giver storage provider
     *
     * @return storage provider you used last
     */

    public String getStorageProvider() {
        return storageProvider;
    }

    /**
     * Method required Must call for setting storage provide to used Upto nougat this library
     *
     * @param storageProvider storageProvider like (tk.jamun.ui.fileprovider)
     */
    public void setStorageProvider(String storageProvider) {
        this.storageProvider = storageProvider;
    }


    public HelperFileFormat() {
        fileType.add("docx");
        fileType.add("txt");
        fileType.add("pdf");
        fileType.add("csv");
        fileType.add("xlsv");
        fileType.add("ppt");
        fileType.add("pptx");
        fileType.add("doc");
        fileType.add("html");
        fileType.add("rtf");
        fileType.add("odt");
        fileType.add("htm");

        imageType.add("jpg");
        imageType.add("jpeg");
        imageType.add("png");
        soundType.add("amr");
        soundType.add("mp3");
        soundType.add("acc");
    }

    public int getDuration(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        return Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
    }

    /**
     * Method used to add file extension as per your need
     *
     * @param fileExtensions File extension string
     */
    public void addYourFileExtensions(String fileExtensions) {
        fileType.add(fileExtensions);
    }

    /**
     * Method used to add audio extension as per your need
     *
     * @param audioExtensions File extension string
     */
    public void addYourAudioExtensions(String audioExtensions) {
        soundType.add(audioExtensions);
    }

    /**
     * Method used to add image extension as per your need
     *
     * @param imageExtensions File extension string
     */
    public void addYourImageExtensions(String imageExtensions) {
        imageType.add(imageExtensions);
    }

    /**
     * Method used for checking availability of image type
     *
     * @param typeOfImage type of image or Extension of image like jpg,jped,png etc.
     * @return status
     */
    public boolean isImageAvail(String typeOfImage) {
        for (String type : imageType) {
            if (type.equals(typeOfImage)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method used for checking availability of image type
     *
     * @param typeOfAudio type of image or Extension of image like mp3,amr,acc etc.
     * @return status
     */
    public boolean isAudioAvail(String typeOfAudio) {
        for (String type : soundType) {
            if (type.equals(typeOfAudio)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method used for checking availability of image type
     *
     * @param typeOfFile type of file or Extension of file like pdf,docs etc.
     * @return status
     */
    public boolean isFileAvail(String typeOfFile) {
        for (String type : fileType) {
            if (type.equals(typeOfFile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    String[] split = DocumentsContract.getDocumentId(uri).split(":");
                    if ("primary".equalsIgnoreCase(split[0])) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(uri)) {
                    return getDataColumn(context, ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri))),
                            null, null);
                } else if (isMediaDocument(uri)) {
                    final String[] split = DocumentsContract.getDocumentId(uri).split(":");
                    Uri contentUri = null;
                    switch (split[0]) {
                        case "image":
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case "video":
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case "audio":
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                            break;
                    }
                    return getDataColumn(context, contentUri, "_id=?", new String[]{split[1]});
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();
                return getDataColumn(context, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            L.logE(e.getMessage());
        }
        return null;
    }

    private MediaExtractor videoExtractor, audioExtractor;
    private MediaMuxer mediaMuxer;

    public boolean merge(File videoFile, File audioFile, File outputFile) {
        try {
            videoExtractor = new MediaExtractor();
            videoExtractor.setDataSource(videoFile.getAbsolutePath());
            MediaFormat videoFormat = null;

            int videoTrackCount = videoExtractor.getTrackCount();
            //vedio max input size
            int frameMaxInputSize = 0;
            int frameRate = 0, videoTrackIndex = -1, audioTrackIndex = -1;
            long videoDuration = 0;

            for (int i = 0; i < videoTrackCount; i++) {
                videoFormat = videoExtractor.getTrackFormat(i);
                String mimeType = videoFormat.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("video/")) {
                    /*
                     * just use the first track
                     */
                    videoTrackIndex = i;
                    frameMaxInputSize = videoFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
                    frameRate = videoFormat.getInteger(MediaFormat.KEY_FRAME_RATE);
                    videoDuration = videoFormat.getLong(MediaFormat.KEY_DURATION);
                    break;
                }
            }

            if (videoTrackIndex < 0) {
                return false;
            }
            audioExtractor = new MediaExtractor();
            audioExtractor.setDataSource(audioFile.getAbsolutePath());
            MediaFormat audioFormat = null;
            int audioTrackCount = audioExtractor.getTrackCount();
            for (int i = 0; i < audioTrackCount; i++) {
                audioFormat = audioExtractor.getTrackFormat(i);
                String mimeType = audioFormat.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("audio/")) {
                    audioTrackIndex = i;
                    break;
                }
            }

            if (audioTrackIndex < 0) {
                return false;
            }

            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            mediaMuxer = new MediaMuxer(outputFile.getAbsolutePath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeVideoTrackIndex = mediaMuxer.addTrack(videoFormat);
            int writeAudioTrackIndex = mediaMuxer.addTrack(audioFormat);
            mediaMuxer.start();

            ByteBuffer byteBuffer = ByteBuffer.allocate(frameMaxInputSize);
            videoExtractor.unselectTrack(videoTrackIndex);
            videoExtractor.selectTrack(videoTrackIndex);

            while (true) {
                int readVideoSampleSize = videoExtractor.readSampleData(byteBuffer, 0);
                if (readVideoSampleSize < 0) {
                    videoExtractor.unselectTrack(videoTrackIndex);
                    break;
                }
                long videoSampleTime = videoExtractor.getSampleTime();
                videoBufferInfo.size = readVideoSampleSize;
                videoBufferInfo.presentationTimeUs = videoSampleTime;
                //videoBufferInfo.presentationTimeUs += 1000 * 1000 / frameRate;
                videoBufferInfo.offset = 0;
                videoBufferInfo.flags = videoExtractor.getSampleFlags();
                mediaMuxer.writeSampleData(writeVideoTrackIndex, byteBuffer, videoBufferInfo);
                videoExtractor.advance();
            }
            long audioPresentationTimeUs = 0;
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();
            audioExtractor.selectTrack(audioTrackIndex);
            /*
             * the last audio presentation time.
             */
            long lastEndAudioTimeUs = 0;
            while (true) {
                int readAudioSampleSize = audioExtractor.readSampleData(byteBuffer, 0);
                if (readAudioSampleSize < 0) {
                    //if end of the stream, unselect
                    audioExtractor.unselectTrack(audioTrackIndex);
                    if (audioPresentationTimeUs >= videoDuration) {
                        //if has reach the end of the video time ,just exit
                        break;
                    } else {
                        //if not the end of the video time, just repeat.
                        lastEndAudioTimeUs += audioPresentationTimeUs;
                        audioExtractor.selectTrack(audioTrackIndex);
                        continue;
                    }
                }

                long audioSampleTime = audioExtractor.getSampleTime();
                audioBufferInfo.size = readAudioSampleSize;
                audioBufferInfo.presentationTimeUs = audioSampleTime + lastEndAudioTimeUs;
                if (audioBufferInfo.presentationTimeUs > videoDuration) {
                    audioExtractor.unselectTrack(audioTrackIndex);
                    break;
                }
                audioPresentationTimeUs = audioBufferInfo.presentationTimeUs;
                audioBufferInfo.offset = 0;
                audioBufferInfo.flags = audioExtractor.getSampleFlags();
                mediaMuxer.writeSampleData(writeAudioTrackIndex, byteBuffer, audioBufferInfo);
                audioExtractor.advance();
            }
            return true;
        } catch (IOException e) {
            L.logE(e.getMessage());
            e.printStackTrace();
        } finally {
            if (mediaMuxer != null) {
                try {
                    mediaMuxer.stop();
                    mediaMuxer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (videoExtractor != null) {
                try {
                    videoExtractor.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (audioExtractor != null) {
                try {
                    audioExtractor.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    public boolean mergeSongs(File mergedFile, File... mp3Files) {
        FileInputStream fisToFinal = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mergedFile);
            fisToFinal = new FileInputStream(mergedFile);
            for (File mp3File : mp3Files) {
                if (!mp3File.exists())
                    continue;
                FileInputStream fisSong = new FileInputStream(mp3File);
                SequenceInputStream sis = new SequenceInputStream(fisToFinal, fisSong);
                byte[] buf = new byte[1024];
                try {
                    for (int readNum; (readNum = fisSong.read(buf)) != -1; )
                        fos.write(buf, 0, readNum);
                } finally {
                    fisSong.close();
                    sis.close();
                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                if (fisToFinal != null) {
                    fisToFinal.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void editFile(String destination, String source, int start, int count) {
        if (source.equalsIgnoreCase(destination)) {
            return;
        }
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            inputChannel = new FileInputStream(new File(source)).getChannel();
            outputChannel = new FileOutputStream(new File(destination)).getChannel();
            inputChannel.transferTo(start, count, outputChannel);
            inputChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputChannel != null)
                    inputChannel.close();
                if (outputChannel != null) outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    /**
     * Method used for calling picker for file opening Tested with most of the device so no compatibility issue
     *
     * @param context      Context
     * @param minmeType    Type of file you want to used
     * @param comeForMulti for Multi Selection purpose put yes
     * @return intent for used
     */
    public Intent getFilePickerIntent(Context context, String minmeType, boolean comeForMulti) {
        Intent intent
                = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(minmeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (comeForMulti) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        if (comeForMulti) {
            sIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        sIntent.putExtra("CONTENT_TYPE", minmeType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);
        Intent chooserIntent;
        if (context.getPackageManager().resolveActivity(sIntent, 0) != null) {
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }
        return chooserIntent;
    }

    /**
     * Method used to delete complete File
     *
     * @param filePath path of the file
     */
    public static void deleteFile(String filePath) {
        if (filePath != null) {
            try {
                File file = new File(filePath);
                file.delete();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Method used to delete complete directory
     *
     * @param directoryName Name of the directory
     */
    public static void deleteDirectory(String directoryName) {
        if (directoryName != null) {
            try {
                File dir = new File(directoryName);
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(dir, children[i]).delete();
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * Method used to open a file into available apps
     *
     * @param activity Context of Class
     * @param filePath Path of File
     * @return Status
     */
    public boolean openFile(Context activity, String filePath) {
        try {
            File file = new File(filePath);
            Uri uri;
            if (CheckOs.checkForNogout()) {
                uri = FileProvider.getUriForFile(activity, storageProvider, file);
            } else {
                uri = Uri.fromFile(file);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent intent1 = Intent.createChooser(intent, "Open Document With");
            if (intent1.resolveActivity(activity.getPackageManager()) != null)
                activity.startActivity(intent1);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Method used to find System Extensions of a MimeType
     *
     * @param mimeType MimeType of a file
     * @return System Extensions
     */
    public String getExtension(String mimeType) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

    /**
     * Method used to find System Extensions of a MimeType
     *
     * @param url MimeType of a file
     * @return System Extensions
     */
    public String getFileExtensionFromUrl(String url) {
        return MimeTypeMap.getFileExtensionFromUrl(url);
    }

    /**
     * Copies one file into the other with the given paths.
     * In the event that the paths are the same, trying to copy one file to the other
     * will cause both files to become null.
     * Simply skipping this step if the paths are identical.
     */
    public void copyFile(String destination, String source) {
        if (source.equalsIgnoreCase(destination)) {
            return;
        }
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            inputChannel = new FileInputStream(new File(source)).getChannel();
            outputChannel = new FileOutputStream(new File(destination)).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputChannel != null)
                    inputChannel.close();
                if (outputChannel != null) outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
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
            String fileType = file.getName().substring(file.getName().lastIndexOf("."));
            if ("png".equals(fileType.toLowerCase())) {
                bitmap.compress(Bitmap.CompressFormat.PNG, UserStorage.getInstance().getCompressions(), out);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, UserStorage.getInstance().getCompressions(), out);
            }
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Method used to save file into cache after checking file Extension for Image, File and Audios
     *
     * @param activity        Context
     * @param fileUri         Uri of the file
     * @param fileNew         New file which override this you can use this on your own
     * @param pathWithoutType without type
     * @param FILE_SIZE_LIMIT Size limit if you want to set should be in KBs
     * @return return status in string for better Errors and S used for for Success.
     */
    public String saveFileCache(Activity activity, Uri fileUri, File fileNew, String pathWithoutType,
                                int FILE_SIZE_LIMIT) {
        String path = getPath(activity, fileUri);
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                String name = file.getName();
                String fileType = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
                if (file.length() < FILE_SIZE_LIMIT) {
                    if (isFileAvail(fileType)
                            || isAudioAvail(fileType)
                            || isImageAvail(fileType)) {
                        fileNew = new File(pathWithoutType + "." + fileType);
                        if (!fileNew.exists()) {
                            fileNew.mkdirs();
                        }
                        copyFile(fileNew.getAbsolutePath(), file.getAbsolutePath());
                        return "S";
                    } else {
                        return "Incorrect file Format";
                    }
                } else {
                    return "File size is must be smaller then " + (FILE_SIZE_LIMIT / 1024) + " MB";
                }
            } else {
                return "File not found";
            }
        } else {
            return "File not found";
        }
    }

    /**
     * Method used to save Only Image into cache after checking Extension
     *
     * @param activity        Context
     * @param fileUri         Uri of the file
     * @param fileNew         New file which override this you can use this on your own
     * @param pathWithoutType without type
     * @param FILE_SIZE_LIMIT Size limit if you want to set should be in KBs
     * @return return status in string for better Errors and S used for for Success.
     */
    public String saveImageCache(Activity activity, Uri fileUri, File fileNew, String pathWithoutType, int FILE_SIZE_LIMIT) {
        String path = getPath(activity, fileUri);
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                String name = file.getName();
                String fileType = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
                if (file.length() < FILE_SIZE_LIMIT) {
                    if (isImageAvail(fileType)) {
                        copyFile(fileNew.getAbsolutePath(), file.getAbsolutePath());
                        return "S";
                    } else {
                        return "Incorrect file Format";
                    }
                } else {
                    return "File size is must be smaller then " + (FILE_SIZE_LIMIT / 1024) + " MB";
                }
            } else {
                return "File not found";
            }
        } else {
            return "File not found";
        }
    }

    /**
     * Calculate file size in format
     *
     * @param fileSize file size in bytes
     * @return Return format
     */
    public String getDisplaySize(long fileSize) {
        if (fileSize >= 1024 * 1024) {
            return (fileSize / 1024 / 1024) + " MB";
        } else if (fileSize < 1024) {
            return fileSize + " Bytes";
        } else {
            fileSize = fileSize / 1024;
            if (fileSize > 100) {
                long mb = fileSize / 1024;
                long kb = fileSize % 1024;
                if (kb != 0) {
                    return mb + "." + String.valueOf(kb).substring(0, 1) + " MB";
                } else {
                    return mb + " MB";
                }
            } else {
                return fileSize + " KB";
            }
        }
    }

    private Date date;
    private long sec, min;

    /**
     * Used to calculate file duration from tile in milliseconds
     *
     * @param fileTime File time in milliseconds
     * @return Time with Proper syntax
     */
    public String getFileDuration(long fileTime) {
        sec = fileTime;
        if (sec > 0) {
            if (sec < 60) {
                if (sec < 10) {
                    return "0:0" + sec;
                } else {
                    return "0:" + sec;
                }
            } else if (sec < 3600) {
                min = sec / 60;
                sec = sec % 60;
                if (sec < 10) {
                    return min + ":0" + sec;
                } else {
                    return min + ":" + sec;
                }
            } else {
                min = sec / 60;
                long hour = min / 60;
                min = min % 60;
                sec = sec % 60;
                if (sec < 10) {
                    return hour + ":" + min + ":0" + sec;
                } else {
                    return hour + ":" + min + ":" + sec;
                }
            }
        } else {
            return "0:00";
        }
    }


    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat
            = new SimpleDateFormat(CONSTANT_DATE);


    /**
     * Method used to get Date saves in milliseconds
     *
     * @param timeStamp Time stamps in milliseconds
     * @return Date
     */
    public String getContentDate(long timeStamp) {
        date = new Date(timeStamp);
        return simpleDateFormat.format(date);
    }

    public boolean isSoundAvail(String typeCome) {
        for (String type : soundType) {
            if (type.equals(typeCome)) {
                return true;
            }
        }
        return false;
    }

    public void getFileFromUri(Context context, @NotNull Uri uri, @NotNull OnEventOccurListener listener) {
        new FileAsync(context, uri, listener).execute();
    }

    static class FileAsync extends AsyncTask<Void, Void, Void> {
        @SuppressLint("StaticFieldLeak")
        private Context context;
        private Uri uri;
        private OnEventOccurListener listeners;
        private String errorMessage;
        private File file;

        public FileAsync(Context context, Uri uri, OnEventOccurListener listeners) {
            this.context = context;
            this.uri = uri;
            this.listeners = listeners;
        }

        private Bitmap getBitmapFromUri(Context context, Uri uri) {
            try {
                ParcelFileDescriptor parcelFileDescriptor = null;
                parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                return image;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap bitmap = getBitmapFromUri(context, uri);
            if (bitmap != null) {
                getFileDetails(bitmap, uri);
            }
            return null;
        }

        private void getFileDetails(Bitmap image, Uri uri) {
            try (Cursor cursor = context.getContentResolver()
                    .query(uri, null, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    String displayName = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    File file = StoragePath.INSTANCE.createSentImageFile(displayName);
                    HelperFileFormat.getInstance().saveImageIntoMemo(image, file);
                    getFileModelFromUri(file);
                }
            }
        }

        public void getFileModelFromUri(File file) {
            if (file.length() < Constants.FILE_SIZE_LIMIT) {
                String fileType = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                if (HelperFileFormat.getInstance().isImageAvail(fileType)) {
                    this.file = file;
                } else {
                    errorMessage = context.getString(R.string.string_message_error_incorrect_format);
                }
            } else {
                errorMessage = context.getString(R.string.string_message_error_file_size);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (file == null) {
                listeners.onErrorResponse(1, errorMessage);
            } else {
                listeners.getEventData(file);
            }
        }

    }

}
