package cc.easyandroid.easypermission;

import android.support.annotation.NonNull;

public interface Permission {

    /**
     * Here to fill in all of this to apply for permission, can be a, can be more.
     *
     * @param permissions one or more permissions.
     * @return {@link Permission}.
     */
    @NonNull
    Permission permission(String... permissions);

    /**
     * Request code.
     *
     * @param requestCode int, the first parameter in callback {@code onRequestPermissionsResult(int, String[],
     *                    int[])}}.
     * @return {@link Permission}.
     */
    @NonNull
    Permission requestCode(int requestCode);

    /**
     * With user privilege refused many times, the Listener will be called back, you can prompt the user
     * permissions role in this method.
     *
     * @param listener {@link RationaleListener}.
     * @return {@link Permission}.
     */
    @NonNull
    Permission rationale(RationaleListener listener);

    /**
     * Request permission.
     */
    void send();

}
