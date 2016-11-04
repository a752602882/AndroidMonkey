package dome.ninebox.com.androidmonkey.adapter;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import dome.ninebox.com.androidmonkey.R;
import dome.ninebox.com.androidmonkey.io.DiskLruCache;
import dome.ninebox.com.androidmonkey.model.MatchDetails;
import dome.ninebox.com.androidmonkey.utils.Utility;

/**
 * Created by Administrator on 2016/4/25.
 */
public class MyRecyclerViewAdapter extends RecyclerViewCursorAdapter<MyRecyclerViewHolder> {





    /**
     * 图片硬盘缓存核心类。
     */
    private DiskLruCache mDiskLruCache;

    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private LruCache<String, Bitmap> mMemoryCache;


    public Context mContext;
    public List<String> mMatch_ids;
    public LayoutInflater mLayoutInflater;

    private LayoutInflater inflater;

    private List<List<MatchDetails>> listMatchDetails = new ArrayList<>();


    /**
     * 记录所有图片的任务。
     */
    private Set<DownLoadBitmapTask> bitmapTaskCollection;


    private List<MatchDetails> mUesrDetails = new ArrayList<>();

    public MyRecyclerViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = LayoutInflater.from(context);
        mContext = context;


        mLayoutInflater = LayoutInflater.from(mContext);
        mMatch_ids = new ArrayList<String>();
        bitmapTaskCollection = new HashSet<>();


        //硬盘缓存处理
        try {
            // 获取图片缓存路径
            File cacheDir = getDiskCacheDir(mContext, "thumb");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            // 创建DiskLruCache实例，初始化缓存数据
            mDiskLruCache = DiskLruCache
                    .open(cacheDir, getAppVersion(mContext), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //内存缓存处理
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.item_feed, parent, false);

        return new MyRecyclerViewHolder(view);

    }


    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(MyRecyclerViewHolder holder, Cursor cursor) {

   /*     if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
*/

        MatchDetails md = new MatchDetails();
        md.setImageUrl(cursor.getString(cursor.getColumnIndex("imageUrl")));
        md.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
        md.setItem_0(cursor.getString(cursor.getColumnIndex("item_0")));
        md.setItem_1(cursor.getString(cursor.getColumnIndex("item_1")));
        md.setItem_2(cursor.getString(cursor.getColumnIndex("item_2")));
        md.setItem_3(cursor.getString(cursor.getColumnIndex("item_3")));
        md.setItem_4(cursor.getString(cursor.getColumnIndex("item_4")));
        md.setItem_5(cursor.getString(cursor.getColumnIndex("item_5")));
        md.setRadiant_win(cursor.getInt(cursor.getColumnIndex("radiant_win")));
        md.setStart_time(cursor.getLong(cursor.getColumnIndex("start_time")));


        holder.user_name.setText(cursor.getString(cursor.getColumnIndex("person_name")));
        holder.hurt.setText(cursor.getString(cursor.getColumnIndex("hero_damage")));
        float kda_info = cursor.getInt(cursor.getColumnIndex("kills")) +
                cursor.getInt(cursor.getColumnIndex("deaths")) +
                cursor.getInt(cursor.getColumnIndex("assists"));
        holder.kda_info.setText(cursor.getInt(cursor.getColumnIndex("kills")) + "/" +
                cursor.getInt(cursor.getColumnIndex("deaths")) + "/" +
                cursor.getInt(cursor.getColumnIndex("assists")));
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        holder.kda.setText((decimalFormat.format(kda_info / cursor.getInt(cursor.getColumnIndex("deaths")))));

        if (md.getRadiant_win() == 0) {
            holder.radiant_win.setBackgroundColor(mContext.getResources().getColor(R.color.item_win));
        } else {
            holder.radiant_win.setBackgroundColor(mContext.getResources().getColor(R.color.item_dir));
        }
        holder.start_time.setText(Utility.getStringDate(md.getStart_time()));




        loadBitmaps(holder.hero_image, md.getImageUrl());
        loadBitmaps(holder.avatar_image, md.getAvatar());
        if (md.getItem_0() != null) {
            loadBitmaps(holder.item0, md.getItem_0());
            loadBitmaps(holder.item1, md.getItem_1());
            loadBitmaps(holder.item2, md.getItem_2());
            loadBitmaps(holder.item3, md.getItem_3());
            loadBitmaps(holder.item4, md.getItem_4());
            loadBitmaps(holder.item5, md.getItem_5());
        }
        //md.setHero_id(hero_id);
      /*  loadBitmaps(holder.hero_image,""+md.getItem_0());
        md.setItem_1(item_1);
        md.setItem_2(item_2);
        md.setItem_3(item_3);
        md.setItem_4(item_4);
        md.setItem_5(item_5);*/


    }


    /**
     * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
     * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
     */
    public void loadBitmaps(ImageView imageView, String imageUrl) {

        Bitmap mLoadingBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.item_null);
        try {
            if (cancelBeforeTask(imageUrl, imageView)) {
                Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
                if (bitmap == null) {

                    DownLoadBitmapTask task = new DownLoadBitmapTask(imageView);
                    AsyncDrawable drawable = new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
                    //!!!!
                    imageView.setImageDrawable(drawable);
                    bitmapTaskCollection.add(task);
                    task.execute(imageUrl);



                } else {
                    if (imageView != null && bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onContentChanged() {

    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    private void bindDefaultFeedItem(int position, MyRecyclerViewHolder holder) {

    }

    public void loadMatchInfo(List<MatchDetails> matchDetailses, int postion) {

    }


    /**
     * 加载图片
     */
    class DownLoadBitmapTask extends AsyncTask<String, Void, Bitmap> {

        private  ImageView imageView;
        private final WeakReference<ImageView> imageViewReference;
        private String imageUrl;

        public DownLoadBitmapTask(ImageView imageView) {
            this.imageView =imageView;
            imageViewReference = new WeakReference<ImageView>(imageView);
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];

            //下载准备
            FileDescriptor fileDescriptor = null;
            FileInputStream fileInputStream = null;
            DiskLruCache.Snapshot snapShot = null;
            // 将缓存数据解析成Bitmap对象
            Bitmap bitmap = null;

            try {
                //生成图片url对应的key
                String key = hashKeyForDisk(imageUrl);
                //查找key对应的缓存
                snapShot = mDiskLruCache.get(key);
                if (snapShot == null) {
                    // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (downloadUrlToStream(imageUrl, outputStream)) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }
                    // 缓存被写入后，再次查找key对应的缓存
                    snapShot = mDiskLruCache.get(key);
                }
                if (snapShot != null) {
                    fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                    fileDescriptor = fileInputStream.getFD();
                }
                // 将缓存数据解析成Bitmap对象
                //  Bitmap bitmap = null;
                if (fileDescriptor != null) {
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                }
                if (bitmap != null) {
                    // 将Bitmap对象添加到内存缓存当中
                    addBitmapToMemoryCache(imageUrl, bitmap);
                }
                //return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileDescriptor == null && fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                    }
                }
            }


            return bitmap;


        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
        /*    if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = (ImageView) imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }*/
            if (isCancelled()) {
                bitmap = null;
            }

            //第二次判断，如果iamgeview里面的task跟这个item根据适配器传进来的正确url启动的task不相同，就不显示图片

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = (ImageView) imageViewReference.get();
                final DownLoadBitmapTask downLoadBitmapTask =
                        getBitmapWorkerTask(imageView);
                if (this == downLoadBitmapTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }

            }

        }

        /**
         * 建立HTTP请求，并获取Bitmap对象。
         *
         * @param urlString    imageUrl 图片的URL地址
         * @param outputStream 解析后的Bitmap对象
         * @return
         */
        private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
            HttpURLConnection urlConnection = null;
            BufferedOutputStream out = null;
            BufferedInputStream in = null;
            try {
                final URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
                out = new BufferedOutputStream(outputStream, 8 * 1024);
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                return true;
            } catch (final IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

    }

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }


    /**
     * 将缓存记录同步到journal文件中。
     */
    public void fluchCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        if (bitmapTaskCollection != null) {
            for (DownLoadBitmapTask task : bitmapTaskCollection) {
                task.cancel(false);
            }
        }
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址。
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取当前应用程序的版本号。
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key    LruCache的键，这里传入图片的URL地址。
     * @param bitmap LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }


    /**
     * 麻蛋，还说写个线程类来处理，不是有异步了吗，麻蛋麻蛋
     * --------------创建一个专用的Drawable的子类来储存任务的引用---------------
     */
    private static DownLoadBitmapTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapTask();
            }
        }
        return null;
    }


    static class AsyncDrawable extends BitmapDrawable {

        private WeakReference<DownLoadBitmapTask> taskWeakReference;

        public AsyncDrawable(Resources res, Bitmap bitmap
                , DownLoadBitmapTask task) {
            super(res, bitmap);
            taskWeakReference = new WeakReference<DownLoadBitmapTask>(task);
        }

        public DownLoadBitmapTask getBitmapTask(){
            return taskWeakReference.get();
        }
    }

    public boolean cancelBeforeTask(String url, ImageView imageView){

        DownLoadBitmapTask task = getBitmapWorkerTask(imageView);

        if(task != null){
            String imgUrl = task.imageUrl;
            if (imgUrl != url || imgUrl == ""){
                task.cancel(true);
            } else {
                return false;
            }
        }

        return true;
    }
}
