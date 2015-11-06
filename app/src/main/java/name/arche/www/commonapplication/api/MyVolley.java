/**
 * Copyright 2013 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package name.arche.www.commonapplication.api;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Helper class that is used to provide references to initialized
 * RequestQueue(s) and ImageLoader(s)
 * 
 * @author Ognyan Bankov
 * 
 */
public class MyVolley {
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;

	private MyVolley() {
		// no instances
	}

	// If your application makes constant use of the network, it's probably most
	// efficient to set up a single instance of RequestQueue that will last the
	// lifetime of your app.
	public static void init(Context context) {	// TIPS： invoked in application  to init volley  !!!
		mRequestQueue = Volley.newRequestQueue(context);

		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
	}

	/**
	 * Log or request TAG
	 */
	public static final String VolleyTAG = "VolleyPatterns";
	
	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null)
			return mRequestQueue;
		else
			throw new IllegalStateException("RequestQueue not initialized");
	}
	
	/**
	 * Adds the specified request to the global queue, if tag is specified then
	 * it is used else Default TAG is used.
	 * 
	 * @param req
	 * @param tag
	 */
	public static <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setRetryPolicy(new DefaultRetryPolicy(5*1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? VolleyTAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	/**
	 * Adds the specified request to the global queue using the Default TAG.
	 * 
	 * @param req
	 */
	public static <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
        addToRequestQueue(req,VolleyTAG);
	}

	/**
	 * Cancels all pending requests by the specified TAG, it is important to
	 * specify a TAG so that the pending/ongoing requests can be cancelled.
	 * 
	 * @param tag
	 */
	public static void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     */
    public static void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(VolleyTAG);
        }
    }
	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}
}
