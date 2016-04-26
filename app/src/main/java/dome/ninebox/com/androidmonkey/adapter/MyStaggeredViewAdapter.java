package dome.ninebox.com.androidmonkey.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import dome.ninebox.com.androidmonkey.R;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MyStaggeredViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder>{

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mDatas;
    private List<Integer> mHeights;


    public MyStaggeredViewAdapter(Context mContext) {

        this.mContext =mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = new ArrayList<>();
        mHeights = new ArrayList<>();
        for (int i ='A';i<='z';i++){
            mDatas.add((char) i + "");
        }
        for (int i =0;i<=mDatas.size();i++){
            mHeights.add((int) (Math.random() * 300) + 200);
        }

    }

    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mLayoutInflater.inflate(R.layout.item_main, parent, false);
        MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {

        if (mOnItemClickListener!=null){
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

            holder.mTextView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {

                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });

        }

        ViewGroup.LayoutParams mLayoutParams = holder.mTextView.getLayoutParams();
        mLayoutParams.height= mHeights.get(position);
        holder.mTextView.setLayoutParams(mLayoutParams);
        holder.mTextView.setText(mDatas.get(position));

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public interface  OnItemClickListener{

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);

    }
    public  OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listenter){
        this.mOnItemClickListener = listenter;
    }
}
