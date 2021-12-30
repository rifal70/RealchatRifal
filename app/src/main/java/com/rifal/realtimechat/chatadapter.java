package com.rifal.realtimechat;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class chatadapter extends RecyclerView.Adapter<chatadapter.Holder> {
    Context mContext;
    ArrayList<String> listss;
    private AdapterOnItemClickListener mListener;
    Handler handler = new Handler();

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public chatadapter(Context mContext, ArrayList<String> listss, AdapterOnItemClickListener mListener) {
        this.mContext = mContext;
        this.listss = listss;
        this.mListener = mListener;
    }

    @Override
    public chatadapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_adapter, parent, false);

        return new chatadapter.Holder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(chatadapter.Holder holder, int position) {
        final String list = listss.get(position);

        String[] arrayStringName = list.split("name=");
        String nameBefore = arrayStringName[1];

        String[] arrayStringName2 = nameBefore.split("\\} \\}");
        String nameAfter = arrayStringName2[0];

        String[] arrayString = list.split("msg=");
        String msgBefore = arrayString[1];

        String[] arrayString2 = msgBefore.split(", name=");
        String msgAfter = arrayString2[0];

        String[] arrayStringDate = list.split("time=");
        String dateBefore = arrayStringDate[1];

        String[] arrayStringDate2 = dateBefore.split("\\} \\}");
        String dateAfter = arrayStringDate2[0];

        holder.tvtxt.setText(nameAfter);
        holder.tvtxtmsg.setText(msgAfter);
        holder.tvtxtdate.setText(dateAfter);
    }


    @Override
    public int getItemCount() {
        return listss.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvtxt, tvtxtmsg, tvtxtdate;
        public Holder(View itemView, AdapterOnItemClickListener listener) {
            super(itemView);
            mListener = listener;
            tvtxt = itemView.findViewById(R.id.tv_txt_name);
            tvtxtmsg = itemView.findViewById(R.id.tv_txt_msg);
            tvtxtdate = itemView.findViewById(R.id.tv_txt_date);
        }

    }

}
