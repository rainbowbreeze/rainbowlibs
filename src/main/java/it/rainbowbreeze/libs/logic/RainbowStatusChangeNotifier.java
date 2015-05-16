package it.rainbowbreeze.libs.logic;

import android.text.TextUtils;

import java.util.HashMap;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;

/**
 * Created by alfredomorresi on 16/05/15.
 */
public class RainbowStatusChangeNotifier {
    private static final String LOG_TAG = RainbowStatusChangeNotifier.class.getSimpleName();

    private final IRainbowLogFacility mLogFacility;
    private final HashMap<String, StatusChangerListener> mListeners;

    public static interface StatusChangerListener {
        void OnStatusChanges(String statusKey);
    }

    public RainbowStatusChangeNotifier(IRainbowLogFacility logFacility) {
        mLogFacility = logFacility;
        mListeners = new HashMap<>();
    }

    public void registerListener(String newListenerId, StatusChangerListener listener) {
        if (TextUtils.isEmpty(newListenerId)) return;
        if (null == listener) return;

        mListeners.put(newListenerId, listener);
    }

    public void unregisterListener(String listenerId) {
        if (TextUtils.isEmpty(listenerId)) return;
        if (mListeners.containsKey(listenerId)) {
            mListeners.remove(listenerId);
        }
    }

    protected void notifyStatusChanged(String statusKey) {
        mLogFacility.v(LOG_TAG, "Sending status change for id " + statusKey);
        for (StatusChangerListener listener : mListeners.values()) {
            listener.OnStatusChanges(statusKey);
        }
    }
}
