package com.joker.sponge.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by joker on 17-2-27.
 */

public class CommentListView  extends LinearLayout{
    private int m_nCharacterNameColor;
    private int m_nContentColor;
    private int m_nDisToContent;
    private String mstrHideListHint;
    private String mstrReplyFormat;
    private float m_nTextSize;
    private int m_nDefaultShowLineNumber;
    private int m_nLineSpace;
    private ModuleTranslater mTranslater;
    private LinearLayout mtoExpandContentPraent;
    private List<CommmentInfo> m_lstComment;
    public CommentListView(Context context) {
        this(context, null);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        setOrientation(LinearLayout.VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommentListView);
        if(typedArray.hasValue(R.styleable.CommentListView_character_color)){
            m_nCharacterNameColor = typedArray.getColor(R.styleable.CommentListView_character_color, Color.rgb(40,140,149));
        }
        if(typedArray.hasValue(R.styleable.CommentListView_contentColor)){
            m_nContentColor = typedArray.getColor(R.styleable.CommentListView_contentColor, Color.rgb(62,71,70));
        }
        if(typedArray.hasValue(R.styleable.CommentListView_hide_list_Hint)){
            mstrHideListHint = typedArray.getString(R.styleable.CommentListView_hide_list_Hint);
        }
        if(typedArray.hasValue(R.styleable.CommentListView_reply_format)){
            mstrReplyFormat = typedArray.getString(R.styleable.CommentListView_reply_format);
        }
        if(typedArray.hasValue(R.styleable.CommentListView_contentTextSize)){
            m_nTextSize = typedArray.getDimension(R.styleable.CommentListView_contentTextSize, 11);
        }
        if(typedArray.hasValue(R.styleable.CommentListView_disToContent)){
            m_nDisToContent = typedArray.getDimensionPixelOffset(R.styleable.CommentListView_disToContent,
                    context.getResources().getDimensionPixelOffset(R.dimen.comment_default_dis_to_content));
        }
        if(typedArray.hasValue(R.styleable.CommentListView_default_show_line_number)){
            m_nDefaultShowLineNumber = typedArray.getInteger(R.styleable.CommentListView_default_show_line_number, 2);
        }
        if(typedArray.hasValue(R.styleable.CommentListView_lineSpace)){
            m_nLineSpace = typedArray.getDimensionPixelOffset(R.styleable.CommentListView_lineSpace,
                    context.getResources().getDimensionPixelOffset(R.dimen.comment_default_lineSpace));
        }
        typedArray.recycle();
    }


    public <T> void bindData(List<T> lstContents, ModuleTranslater translater){
        if(translater == null || (lstContents == null || lstContents.size() < 0)){
            return;
        }
        if(m_lstComment == null){
            m_lstComment = new ArrayList<CommmentInfo>();
        }
        m_lstComment.clear();
        removeAllViews();

        for( T comment: lstContents){
            m_lstComment.add(translater.translateFromSrouce(comment));
        }
        fillContentView();
    }

    private void fillContentView(){
        for(int i = 0; i < m_lstComment.size(); i++){
            addItemCommentView(m_lstComment.get(i), i);
        }
    }

    private void addItemCommentView(CommmentInfo commmentInfo, int index){


        LinearLayout contentGroup = new LinearLayout(getContext());
        contentGroup.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if(index == 0){
            params.setMargins(0, 0, 0, 0);
        }else{
            params.setMargins(0, m_nLineSpace ,0, 0);
        }

        TextView tvCharacterName = new TextView(getContext());
        LinearLayout.LayoutParams tvCharacterNameParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tvCharacterName.setTextColor(m_nCharacterNameColor);
        tvCharacterName.getPaint().setTextSize(m_nTextSize);
        tvCharacterName.setLayoutParams(tvCharacterNameParams);
        if(TextUtils.isEmpty(commmentInfo.getParentCommenterName())){
            tvCharacterName.setText(commmentInfo.getCurrentCommmenterName()+":");
        }else{
            tvCharacterName.setTextColor(m_nContentColor);
            String parentName = commmentInfo.getParentCommenterName();
            String charactName = commmentInfo.getCurrentCommmenterName();
            String name = String.format(mstrReplyFormat, charactName,
                    parentName);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(name);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.gray_285c95)), 1, parentName.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.gray_285c95)), name.length() - parentName.length(), name.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvCharacterName.setText(spannableStringBuilder);
        }
        TextView tvCommentContent = new TextView(getContext());
        LinearLayout.LayoutParams tvCommentContentParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tvCommentContent.setTextColor(m_nContentColor);
        tvCommentContent.getPaint().setTextSize(m_nTextSize);
        tvCommentContent.setText(commmentInfo.getContent());
        tvCommentContentParams.setMargins(m_nDisToContent, 0, 0, 0);
        tvCommentContent.setLayoutParams(tvCommentContentParams);

        //　添加展开评论项
        if(index == m_nDefaultShowLineNumber){
            // 先添加展开评论项
            TextView tvExpand = new TextView(getContext());
            LinearLayout.LayoutParams tvExpandNameParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tvExpand.setTextColor(m_nCharacterNameColor);
            tvExpand.getPaint().setTextSize(m_nTextSize);
            tvExpandNameParams.setMargins(0,0,0,0);
            tvExpand.setLayoutParams(tvExpandNameParams);
            tvExpand.setText(String.format(mstrHideListHint, m_lstComment.size() - index -1));
            tvExpand.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mtoExpandContentPraent != null && mtoExpandContentPraent.getVisibility() == View.GONE){
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)mtoExpandContentPraent.getLayoutParams();
                        params1.setMargins(0, 0, 0, 0);
                        mtoExpandContentPraent.setLayoutParams(params1);
                        mtoExpandContentPraent.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams params2  = (LinearLayout.LayoutParams)((LinearLayout)view.getParent()).getLayoutParams();
                        params2.setMargins(0, 0, 0, 0);
                        ((LinearLayout)view.getParent()).setLayoutParams(params2);
                        mtoExpandContentPraent.setLayoutParams(params1);
                        view.setVisibility(View.GONE);
                    }
                }
            });
            contentGroup.addView(tvExpand, tvExpandNameParams);
            this.addView(contentGroup, params);

            mtoExpandContentPraent = new LinearLayout(getContext());
            mtoExpandContentPraent.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams expandPrams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            expandPrams.setMargins(0, m_nLineSpace, 0, 0);
            mtoExpandContentPraent.setLayoutParams(expandPrams);
            mtoExpandContentPraent.setVisibility(View.GONE);
        }

        if(index == m_nDefaultShowLineNumber){
            if(mtoExpandContentPraent != null){
                LinearLayout expandFirst = new LinearLayout(getContext());
                expandFirst.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams expandFirstPrams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                expandFirstPrams.setMargins(0, m_nLineSpace, 0, 0);
                expandFirst.setLayoutParams(expandFirstPrams);
                expandFirst.addView(tvCharacterName);
                expandFirst.addView(tvCommentContent);
                mtoExpandContentPraent.addView(expandFirst);
                this.addView(mtoExpandContentPraent, mtoExpandContentPraent.getLayoutParams());
            }
        }else if(index > m_nDefaultShowLineNumber){
            contentGroup.addView(tvCharacterName, tvCharacterNameParams);
            contentGroup.addView(tvCommentContent, tvCommentContentParams);
            mtoExpandContentPraent.addView(contentGroup, params);
        }else{
            contentGroup.addView(tvCharacterName, tvCharacterNameParams);
            contentGroup.addView(tvCommentContent, tvCommentContentParams);
            this.addView(contentGroup, params);
        }
    }

    public void setTranslater(ModuleTranslater translater) {
        mTranslater = translater;
    }

    public static class CommmentInfo{
        private String parentCommenterName;
        private String currentCommmenterName;
        private int commentId;
        private String content;
        public CommmentInfo(){

        }
        public String getParentCommenterName() {
            return parentCommenterName;
        }

        public void setParentCommenterName(String parentCommenterName) {
            this.parentCommenterName = parentCommenterName;
        }

        public String getCurrentCommmenterName() {
            return currentCommmenterName;
        }

        public void setCurrentCommmenterName(String currentCommmenterName) {
            this.currentCommmenterName = currentCommmenterName;
        }

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public interface ModuleTranslater<T>{
        CommmentInfo translateFromSrouce(T obj);
    }

}
