package com.unity3d.player;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Process;

import androidx.appcompat.app.AppCompatActivity;

public class UnityPlayerActivity extends BaseActivity implements IUnityPlayerLifecycleEvents
{
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    // Override this in your custom UnityPlayerActivity to tweak the command line arguments passed to the Unity Android Player
    // The command line arguments are passed as a string, separated by spaces
    // UnityPlayerActivity calls this from 'onCreate'
    // Supported: -force-gles20, -force-gles30, -force-gles31, -force-gles31aep, -force-gles32, -force-gles, -force-vulkan
    // See https://docs.unity3d.com/Manual/CommandLineArguments.html
    // @param cmdLine the current command line arguments, may be null
    // @return the modified command line string or null
    protected String updateUnityCommandLineArguments(String cmdLine)
    {
        return cmdLine;
    }

    // Setup activity layout
    @Override public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        //String cmdLine = updateUnityCommandLineArguments(getIntent().getStringExtra("unity"));
        //getIntent().putExtra("unity", cmdLine);

        Log.d("UnityPlayerActivity", "1");
        mUnityPlayer = new UnityPlayer(thisContext, this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();
        Log.d("UnityPlayerActivity", "2");
    }

    // When Unity player unloaded move task to background
    @Override public void onUnityPlayerUnloaded() {
        Log.d("UnityPlayerActivity", "3");
        moveTaskToBack(true);
    }

    // Callback before Unity player process is killed
    @Override public void onUnityPlayerQuitted() {
        Log.d("UnityPlayerActivity", "4");
    }

    @Override protected void onNewIntent(Intent intent)
    {
        Log.d("UnityPlayerActivity", "5");
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
        mUnityPlayer.newIntent(intent);
    }

    // Quit Unity
    @Override public void onDestroy ()
    {
        Log.d("UnityPlayerActivity", "6");
        mUnityPlayer.destroy();
        super.onDestroy();
    }

    // If the activity is in multi window mode or resizing the activity is allowed we will use
    // onStart/onStop (the visibility callbacks) to determine when to pause/resume.
    // Otherwise it will be done in onPause/onResume as Unity has done historically to preserve
    // existing behavior.
    @Override public void onStop()
    {
        Log.d("UnityPlayerActivity", "7");
        super.onStop();

        if (!MultiWindowSupport.getAllowResizableWindow(thisContext))
            return;

        mUnityPlayer.pause();
    }

    @Override public void onStart()
    {
        Log.d("UnityPlayerActivity", "8");
        super.onStart();

        if (!MultiWindowSupport.getAllowResizableWindow(thisContext)) {
            Log.d("UnityPlayerActivity", "8-1");
            return;
        }

        Log.d("UnityPlayerActivity", "8-2");
        mUnityPlayer.resume();
        Log.d("UnityPlayerActivity", "8-3");
    }

    // Pause Unity
    @Override public void onPause()
    {
        Log.d("UnityPlayerActivity", "9");
        super.onPause();

        MultiWindowSupport.saveMultiWindowMode(thisContext);

        if (MultiWindowSupport.getAllowResizableWindow(thisContext))
            return;

        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override public void onResume()
    {
        Log.d("UnityPlayerActivity", "10");
        super.onResume();

        Log.d("UnityPlayerActivity", "10-1");
        if (MultiWindowSupport.getAllowResizableWindow(thisContext) && !MultiWindowSupport.isMultiWindowModeChangedToTrue(thisContext)) {
            Log.d("UnityPlayerActivity", "10-2");
            return;
        }

        Log.d("UnityPlayerActivity", "10-3");
        mUnityPlayer.resume();
        //mUnityPlayer.postInvalidate();
        Log.d("UnityPlayerActivity", "10-4");
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        Log.d("UnityPlayerActivity", "11");
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        Log.d("UnityPlayerActivity", "12");
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        Log.d("UnityPlayerActivity", "13");
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        Log.d("UnityPlayerActivity", "14");
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        Log.d("UnityPlayerActivity", "15");
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     {
        Log.d("UnityPlayerActivity", "16");
        return mUnityPlayer.onKeyUp(keyCode, event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   {
        Log.d("UnityPlayerActivity", "17");
        return mUnityPlayer.onKeyDown(keyCode, event); }
    @Override public boolean onTouchEvent(MotionEvent event)          {
        Log.d("UnityPlayerActivity", "18");
        return mUnityPlayer.onTouchEvent(event); }
    @Override public boolean onGenericMotionEvent(MotionEvent event)  {
        Log.d("UnityPlayerActivity", "19");
        return mUnityPlayer.onGenericMotionEvent(event); }
}
