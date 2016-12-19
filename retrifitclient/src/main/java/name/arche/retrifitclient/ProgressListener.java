package name.arche.retrifitclient;
/**
 * Created by Arche on 2016/11/1.
 */

public interface ProgressListener {
    void onProgress(long progress, long total, boolean done);
}