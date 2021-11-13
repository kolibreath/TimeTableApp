package com.kolibreath.timetableapp.base

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

object LiveDataBus {

    private val map = HashMap<String, MyMutableLiveData<Any>>()

    fun <T> get(key: String): MyMutableLiveData<T> {
        val liveData = map[key]
        if (liveData == null) map[key] = MyMutableLiveData<Any>()
        return map[key] as MyMutableLiveData<T>
    }

    class MyMutableLiveData<T>: MutableLiveData<T>() {

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            hook(observer)
        }

        fun setValue(key:String, value: T) {
            if(Looper.getMainLooper() == Looper.myLooper()) {
                get<T>(key).setValue(value)
            }else {
                get<T>(key).postValue(value)
            }
        }

        // 通过当前的LiveData实例找到mObserver -> mObserver迭代器 -> mObserver.mLastVersion
        private fun hook(observer: Observer<in T>) {
            val mObserversField = LiveData::class.java.getDeclaredField("mObservers").apply {
                isAccessible = true
            }
            val mObservers = mObserversField.get(this)

            // 从mObservers中获取get方法
            val getMethod  = mObservers::class.java.getDeclaredMethod("get", Any::class.java).apply {
                isAccessible = true
            }
            val entry = getMethod.invoke(mObservers, observer)
            val lifeBoundObserver = (entry as Map.Entry<*, *>).value!!
            val observerWrapperClazz = lifeBoundObserver::class.java.superclass

            val mLastVersionField = observerWrapperClazz.getDeclaredField("mLastVersion")
                .apply {
                    isAccessible = true
                }
            val mVersionField = LiveData::class.java.getDeclaredField("mVersion")
                .apply {
                    isAccessible = true
                }

            val mVersion = mVersionField.get(this) as Int
            mLastVersionField.set(lifeBoundObserver, mVersion)
        }
    }

}