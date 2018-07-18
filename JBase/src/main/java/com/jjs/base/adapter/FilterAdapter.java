package com.jjs.base.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Filter;
import android.widget.Filterable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 说明：会获取当前数据保存作为原始数据，所以一开始应设定最完整数据
 * 使用： mAdapter.getFilter().filter(search);
 * Created by jjs on 2018/7/5.
 */

public abstract class FilterAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> implements Filterable {
    private SearchFilter mSearchFilter;
    private List<T> mOrginalList;//原始数据
    private final Object _lock = new Object();// 同步锁？不太懂

    public FilterAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }


    @Override
    public Filter getFilter() {
        if (mSearchFilter == null) {
            mSearchFilter = new SearchFilter();
        }
        return mSearchFilter;
    }


    // 内部类：数据过滤器
    class SearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // 定义过滤规则
            FilterResults filterResults = new FilterResults();

            // 保存原始数据
            if (mOrginalList == null) {
                synchronized (_lock) {
                    mOrginalList = new ArrayList<T>(getData());
                }
            }

            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                synchronized (_lock) {
                    filterResults.values = mOrginalList;
                    filterResults.count = mOrginalList.size();
                }
            } else {

                // 否则过滤出新数据
                String filterString = constraint.toString().trim()
                        .toLowerCase(Locale.US);// 过滤首尾空白，小写过滤
                ArrayList<T> newValues = new ArrayList<T>();

                for (T vo : mOrginalList) {
                    if (vo.toString().toLowerCase(Locale.US)
                            .contains(filterString)) {
                        newValues.add(vo);
                    }
                    filterResults.values = newValues;
                    filterResults.count = newValues.size();
                }
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            setNewData((ArrayList<T>) results.values);// 更新适配器的数据
            notifyDataSetChanged();
        }
    }


}
