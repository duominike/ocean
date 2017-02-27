package com.joker.ocean.testview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.joker.ocean.R;
import com.joker.ocean.base.BaseActivity;
import com.joker.sponge.customview.CommentListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 17-2-27.
 */

public class TextCommentListActivity extends BaseActivity {
    private ListView m_lvComments;
    private List<Comment> m_lstComment = new ArrayList<Comment>();
    private CommentAdapter mCommentAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list_layout);
        createData();
        m_lvComments = (ListView) findViewById(R.id.lv_comment);
        mCommentAdapter = new CommentAdapter();
        m_lvComments.setAdapter(mCommentAdapter);
    }

    private void createData(){
        for(int i=0; i< 10 ;i++){
            m_lstComment.add(new Comment());
        }
    }

    class CommentAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return m_lstComment.size();
        }

        @Override
        public Object getItem(int i) {
            return m_lstComment.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if(view == null){
                view = View.inflate(TextCommentListActivity.this, R.layout.item_comment_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.detailView = (CommentListView) view.findViewById(R.id.commentlistview);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.detailView.bindData(m_lstComment.get(i).mCommentDetails, new CommentListView.ModuleTranslater<CommentDetail>() {
                @Override
                public CommentListView.CommmentInfo translateFromSrouce(CommentDetail detail) {
                    CommentListView.CommmentInfo result = new CommentListView.CommmentInfo();
                    result.setCommentId(detail.commentId);
                    result.setContent(detail.content);
                    result.setCurrentCommmenterName(detail.currentCommmenterName);
                    result.setParentCommenterName(detail.parentCommenterName);
                    return result;
                }
            });
            return view;
        }

        class ViewHolder{
            CommentListView detailView;
        }
    }

    class Comment {
        public List<CommentDetail> mCommentDetails;
        public Comment(){
            mCommentDetails = new ArrayList<CommentDetail>();
            for(int i = 0; i < 25 ; i++){
                CommentDetail detail = new CommentDetail();
                if(i % 2 == 0){
                    detail.currentCommmenterName = "Character" + i;
                    detail.commentId = i;
                }else{
                    detail.parentCommenterName = "Character" + i;
                    detail.commentId = i;
                    detail.currentCommmenterName = "Character" +(i-1);
                }

                detail.content = "随便说点儿什么吧";
                mCommentDetails.add(detail);

            }
        }
    }

    class CommentDetail {
        public String parentCommenterName;
        public String currentCommmenterName;
        public int commentId;
        public String content;
    }
}
