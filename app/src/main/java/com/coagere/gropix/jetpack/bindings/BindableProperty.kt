package com.coagere.gropix.jetpack.bindings

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

class BindableProperty<T>(
    initial: T,
    private val observable: ObservableViewModel,
    private val id: Int
) : ObservableProperty<T>(initial) {

    override fun beforeChange(
        property: KProperty<*>,
        oldValue: T,
        newValue: T
    ): Boolean = oldValue != newValue

    override fun afterChange(
        property: KProperty<*>,
        oldValue: T,
        newValue: T
    ) = observable.notifyPropertyChanged(id)
}

fun <T> ObservableViewModel.bindable(
    initial: T,
    id: Int
): BindableProperty<T> = BindableProperty(initial, this, id)