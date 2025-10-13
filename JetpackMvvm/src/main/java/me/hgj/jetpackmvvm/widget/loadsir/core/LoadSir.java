package me.hgj.jetpackmvvm.widget.loadsir.core;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import me.hgj.jetpackmvvm.widget.loadsir.LoadSirUtil;
import me.hgj.jetpackmvvm.widget.loadsir.callback.Callback;
import me.hgj.jetpackmvvm.widget.loadsir.target.ActivityTarget;
import me.hgj.jetpackmvvm.widget.loadsir.target.ITarget;
import me.hgj.jetpackmvvm.widget.loadsir.target.ViewTarget;

/**
 * Description:TODO
 * Create Time:2017/9/2 16:36
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class LoadSir {
    private static volatile LoadSir loadSir;
    private Builder builder;

    public static LoadSir getDefault() {
        if (loadSir == null) {
            synchronized (LoadSir.class) {
                if (loadSir == null) {
                    loadSir = new LoadSir();
                }
            }
        }
        return loadSir;
    }

    private LoadSir() {
        this.builder = new Builder();
    }

    private void setBuilder(@NonNull Builder builder) {
        this.builder = builder;
    }

    private LoadSir(Builder builder) {
        this.builder = builder;
    }

    public LoadService register(@NonNull Object target) {
        return register(target, null, null);
    }

    public LoadService register(Object target, Callback.OnReloadListener onReloadListener) {
        return register(target, onReloadListener, null);
    }

    public <T> LoadService register(Object target, Callback.OnReloadListener onReloadListener, Convertor<T>
            convertor) {
        ITarget targetContext = LoadSirUtil.getTargetContext(target, builder.getTargetContextList());
        LoadLayout loadLayout = targetContext.replaceView(target, onReloadListener);
        return new LoadService<>(convertor, loadLayout, builder);
    }

    public  Callback getEmptyCallBack(){
        return builder.emptyCallback;
    }

    public  Callback getErrorCallBack(){
        return builder.errorCallback;
    }

    public  Callback getLoadingCallBack(){
        return builder.loadingCallback;
    }

    public static Builder beginBuilder() {
        return new Builder();
    }

    public static class Builder {
        private List<Callback> callbacks = new ArrayList<>();
        private Callback emptyCallback;
        private Callback errorCallback;
        private Callback loadingCallback;
        private List<ITarget> targetContextList = new ArrayList<>();
        private Class<? extends Callback> defaultCallback;

        {
            targetContextList.add(new ActivityTarget());
            targetContextList.add(new ViewTarget());
        }

        public Builder addCallback(@NonNull Callback callback) {
            callbacks.add(callback);
            return this;
        }

        public Builder setEmptyCallBack(@NonNull Callback callback){
            callbacks.add(callback);
            emptyCallback = callback;
            return this;
        }

        public Builder setErrorCallBack(@NonNull Callback callback){
            callbacks.add(callback);
            errorCallback = callback;
            return this;
        }

        public Builder setLoadingCallBack(@NonNull Callback callback){
            callbacks.add(callback);
            loadingCallback = callback;
            return this;
        }


        /**
         * @param targetContext
         * @return Builder
         * @since 1.3.8
         */
        public Builder addTargetContext(ITarget targetContext) {
            targetContextList.add(targetContext);
            return this;
        }

        public List<ITarget> getTargetContextList() {
            return targetContextList;
        }

        public Builder setDefaultCallback(@NonNull Class<? extends Callback> defaultCallback) {
            this.defaultCallback = defaultCallback;
            return this;
        }

        List<Callback> getCallbacks() {
            return callbacks;
        }

        Class<? extends Callback> getDefaultCallback() {
            return defaultCallback;
        }

        public void commit() {
            getDefault().setBuilder(this);
        }

        public LoadSir build() {
            return new LoadSir(this);
        }

    }
}
