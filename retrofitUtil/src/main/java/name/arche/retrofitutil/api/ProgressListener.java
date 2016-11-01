package name.arche.retrofitutil.api;

/**
 * Created by Arche on 2016/11/1.
 */

public interface ProgressListener {
    void onProgress(long progress, long total, boolean done);
}