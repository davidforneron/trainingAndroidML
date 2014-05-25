package com.meli.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ImageDownloader implements IObservable{

    private final static String LOGTAG = ImageDownloader.class.getSimpleName();

    public static final int DOWNLOAD_COMPLETE = 0;
    public static final int DOWNLOAD_FAILED = -1;


    // Sets the initial threadpool size to 8
    private static final int CORE_POOL_SIZE = 8;

    // Sets the maximum threadpool size to 8
    private static final int MAXIMUM_POOL_SIZE = 8;

    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private static ImageDownloader mInstance;

    private ArrayList<IObserver> observers = new ArrayList<IObserver>();
    private Handler mHandler;

    // A queue of Runnables
    private final BlockingQueue<Runnable> mDownloadQueue;

    private final ThreadPoolExecutor mDownloadThreadPool;

    // A queue of PhotoManager tasks. Tasks are handed to a ThreadPool.
    private final Queue<ImageTask> mImageTaskWorkQueue;

    static  {
        // Creates a single static instance of PhotoManager
        mInstance = new ImageDownloader();
    }

    private ImageDownloader() {

        // Instantiates the queue of Runnables as a LinkedBlockingQueue
        mDownloadQueue = new LinkedBlockingQueue<Runnable>();

        mImageTaskWorkQueue = new LinkedBlockingQueue<ImageTask>();

        mDownloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT, mDownloadQueue);


        // Defines a Handler object that's attached to the UI thread
        mHandler = new Handler(Looper.getMainLooper()) {
            /*
             * handleMessage() defines the operations to perform when
             * the Handler receives a new Message to process.
             */
            @Override
            public void handleMessage(Message inputMessage) {
                // Gets the image task from the incoming Message object.
                ImageTask imageTask = (ImageTask) inputMessage.obj;
                notifyObservers(imageTask);
                recycleTask(imageTask);
                Log.i(LOGTAG, "image downloaded");
            }
        };

    }

    /**
     * Returns the ImageDownloader object
     * @return The global ImageDownloader object
     */
    public static ImageDownloader getInstance() {
        return mInstance;
    }

    /**
     * Starts an image download
     *
     * @param imageView The ImageView that will get the resulting Bitmap
     * @param cacheFlag Determines if caching should be used
     * @return The task instance that will handle the work
     */
    public void startDownload(String id, String imageUrl) {

        /*
         * Gets a task from the pool of tasks, returning null if the pool is empty
         */
        ImageTask downloadTask = mImageTaskWorkQueue.poll();

        // If the queue was empty, create a new task instead.
        if (null == downloadTask) {
            downloadTask = new ImageTask(id, imageUrl);
        } else {
            downloadTask.setId(id);
            downloadTask.setImageUrl(imageUrl);
        }
        mInstance.mDownloadThreadPool.execute(downloadTask);
    }

    /**
     * Recycles tasks by calling their internal recycle() method and then putting them back into
     * the task queue.
     * @param downloadTask The task to recycle
     */
    void recycleTask(ImageTask downloadTask) {

        // Frees up memory in the task
        downloadTask.recycle();

        // Puts the task object back into the queue for re-use.
        mImageTaskWorkQueue.offer(downloadTask);
    }


    @Override
    public void registerObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
    }

    public void notifyObservers(ImageTask imageTask) {
        for (IObserver observer : observers) {
            System.out.println("Notifying Observers");
            observer.update(imageTask);
        }
    }

    public void handleState(ImageTask imageTask, int state) {
        switch (state) {
        case DOWNLOAD_COMPLETE:
            // Gets a Message object, stores the state in it, and sends it to the Handler
            Message completeMessage = mHandler.obtainMessage(state, imageTask);
            completeMessage.sendToTarget();
            break;
        }
    }

}
