package com.jjs.demo;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.jjs.R;
import com.jjs.base.utils.recyclerview.DivDecoration;
import com.jjs.base.utils.recyclerview.DraggableAdapter;
import com.jjs.base.utils.recyclerview.ExpandableAdapter;
import com.jjs.base.utils.recyclerview.QuickAdapter;
import com.jjs.base.utils.recyclerview.QuickHolder;
import com.jjs.base.widget.LoadingDialog;
import com.jjs.bean.L0;
import com.jjs.bean.L1;
import com.jjs.bean.L3;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 说明：使用文档：http://www.jianshu.com/p/b343fcff51b0
 * Created by aa on 2017/10/28.
 */

public class BaseQuickAdapterDemo extends com.jjs.base.base.BaseActivity {
    @BindView(R.id.rv_base)
    RecyclerView mRvBase;

    List<String> mList = new ArrayList<>();
    QuickAdapter adapter;

    @Override
    protected void onActivityResult(int requestCode, Intent data) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick);
        ButterKnife.bind(this);
        //准备数据
        for (int i = 0; i < 20; i++) {
            mList.add("数据" + i);
        }
        //绑定数据

        initBaseQuickAdapter();//基础使用
        //initMultiType();//基于QuickAdapter实现 多布局
        //initDraggableAdapter();//拖拽布局和右滑删除
        //initExpandableAdapter();
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.init(BaseQuickAdapterDemo.this);
                LoadingDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Intent intent=new Intent(BaseQuickAdapterDemo.this, XXXX.class);
        //   startActivity(intent);
        LoadingDialog.show();
    }

    /**
     * 基础使用
     */
    private void initBaseQuickAdapter() {
        mRvBase.setLayoutManager(new LinearLayoutManager(this));
        mRvBase.addItemDecoration(new DivDecoration(this));
        adapter = new QuickAdapter<String>(R.layout.adapter_quick, mList) {
            @Override
            public void _convert(QuickHolder holder, String data) {
                holder.setText(R.id.tv_title, data);
                //子控件的点击事件，通过setOnItemChildClickListener回调
                holder.addOnClickListener(R.id.tv_title);
                //子控件的点击事件，通过setOnItemChildLongClickListener回调
                holder.addOnClickListener(R.id.tv_title);
            }
        };
        //item点击监听
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //通过getViewByPosition 获取指定item中的子控件
                TextView textView = (TextView) adapter.getViewByPosition(position, R.id.tv_title);
            }
        });
        //item长按监听
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                return false;
            }
        });
        //子控件点击监听
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        //子控件长按监听
        adapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                return false;
            }
        });

        //无数据时，设置空数据
        //如果用网格布局的话，设置空布局就不能给全屏，可以使用瀑布流布局
        //使用报 please bind recyclerView first!错误，猜测需要使用到rootView导致的，所以基本无用
        //adapter.setEmptyView(R.layout.activity_view_demo);

        /**
         * 动画（默认渐显）
         * ALPHAIN 渐显、
         * SCALEIN 缩放、
         * SLIDEIN_BOTTOM 从下到上、
         * SLIDEIN_LEFT 从左到右、
         * SLIDEIN_RIGHT 从右到左
         */
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //动画默认只执行一次,如果想重复执行可设置
        //adapter.isFirstOnly(false);
        //设置不显示动画数量（用于：首屏进入动画是同时的，干脆设置长度那几个不执行）
        adapter.setNotDoAnimationCount(5);
        /**
         * header和footer
         * removeHeaderView 删除指定header；另外还有删除footer和删除所有
         *
         */
        View headView = View.inflate(this, R.layout.ccc, null);
        ((TextView) headView.findViewById(R.id.tv_title)).setText("HeaderView");
        View footView = View.inflate(this, R.layout.ccc, null);
        ((TextView) footView.findViewById(R.id.tv_title)).setText("FooterView");
        //设置头部
        adapter.addHeaderView(headView);
        adapter.addFooterView(footView);
        //默认头部是占满一整行的，可以配置如下设置
        adapter.setHeaderViewAsFlow(true);
        //默认出现了头部就不会显示Empty，和尾部，配置以下方法也支持同时显示：
        //setHeaderAndEmpty       setHeaderFooterEmpty
        /**
         * 刷新
         */
        //是否打开上拉加载，当下拉刷新时，需要关闭，因为不能同时存在
        adapter.setEnableLoadMore(true);
        //预加载、当滑动到倒数第几个item触发刷新监听（默认为1）
        adapter.setPreLoadNumber(1);
        //设置自定义上拉加载布局
        //上拉加载监听
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.e("EEEE", "触发了加载！");
                //加载完成，还有下页数据
                //adapter.loadMoreComplete();
                //加载完成，没有下页数据
                // adapter.loadMoreEnd();
                //adapter.loadMoreEnd(true);//设置是否展示end
                //加载失败，再次滑动不会触发，需手动点击
                //adapter.loadMoreFail();
            }
        }, mRvBase);
        //下拉加载（用于聊天软件的历史纪录）
        adapter.setUpFetchEnable(false);
        //下拉加载的触发位置
        adapter.setStartUpFetchPosition(2);
        //下拉加载触发监听
        adapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
            @Override
            public void onUpFetch() {
                //需要及时调用setUpFetching，否则onUpFetch也许会调用多次
                adapter.setUpFetching(true);
                Log.e("EEEE", "触发了onUpFetch！");
                mRvBase.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //处理完数据后，再行释放
                        adapter.setUpFetching(false);
                    }
                }, 1000);
            }
        });
        mRvBase.setAdapter(adapter);

    }

    /**
     * 多布局
     */
    private void initMultiType() {
        mRvBase.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new QuickAdapter<String>() {
            @Override
            public void initMultiType() {
                super.initMultiType();
                setMultiTypeDelegate(new MultiTypeDelegate<String>() {
                    @Override
                    protected int getItemType(String entity) {
                        //根据你的实体类来判断布局类型
                        return entity.equals("数据2") || entity.equals("数据6") ? 1 : 2;
                    }
                });
                //Step.2
                getMultiTypeDelegate()
                        .registerItemType(1, R.layout.adapter_quick)
                        .registerItemType(2, R.layout.ccc);

            }

            @Override
            public void _convert(final QuickHolder holder, final String data) {
                switch (holder.getItemViewType()) {
                    case 1:
                        holder.setText(R.id.tv_title, "ssssss:" + data);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.showShort(data);//触发了的
                                //后面代码有问题
                                int pos = holder.getAdapterPosition();
                            }
                        });
                        break;
                    case 2:
                        holder.setText(R.id.tv_title, data);
                        break;
                }
            }
        };
        //考虑到grid布局时的复用，进行spanSize的配置,管理器为StaggeredGridLayoutManager时有点问题
        //总结，要用空布局只能linear，要用多布局不能Staggered
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return adapter.getItemViewType(position) == 1 ? 2 : 1;
            }
        });
        adapter.setNewData(mList);
        mRvBase.setAdapter(adapter);

    }


    /**
     * 拖动布局和右滑删除布局
     */
    private void initDraggableAdapter() {
        //默认不支持多个不同的 ViewType 之间进行拖拽，如果开发者有所需求：
        //重写ItemDragAndSwipeCallback里的onMove()方法，return true即可
        mRvBase.setLayoutManager(new GridLayoutManager(this, 4));
        DraggableAdapter draggableAdapter = new DraggableAdapter<String>(R.layout.adapter_quick, mList) {
            @Override
            public void _convert(QuickHolder holder, String data) {
                holder.setText(R.id.tv_title, data);
            }
        };
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(draggableAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRvBase);
        // 开启拖拽
        draggableAdapter.enableDragItem(itemTouchHelper, R.id.tv_title, true);
        //拖拽监听
        draggableAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

            }
        });

        // 开启滑动删除
        draggableAdapter.enableSwipeItem();
        draggableAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });
        mRvBase.setAdapter(draggableAdapter);

    }

    /**
     * 多级菜单栏
     */
    private void initExpandableAdapter() {
        ArrayList<MultiItemEntity> entityList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            L0 l0 = new L0("L0:" + i);
            L1 l1 = new L1("L1:" + i);
            for (int j = 0; j < 3; j++) {
                l1.addSubItem(new L3("L3:" + j));
            }
            l0.addSubItem(l1);
            entityList.add(l0);
        }
        mRvBase.setLayoutManager(new LinearLayoutManager(this));
        ExpandableAdapter expandableAdapter = new ExpandableAdapter(entityList) {
            @Override
            public void initItemTypes() {
                addItemType(0, R.layout.ccc);
                addItemType(1, R.layout.ccc);
                addItemType(2, R.layout.adapter_quick);
            }

            @Override
            public void _convert(final QuickHolder holder, final MultiItemEntity data) {
                switch (holder.getItemViewType()) {
                    case 0:
                        holder.setText(R.id.tv_title, ((L0) data).getL0str());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = holder.getAdapterPosition();
                                if (((L0) data).isExpanded()) {
                                    collapse(pos, false);
                                } else {
                                    expand(pos, false);
                                }
                            }
                        });
                        break;
                    case 1:
                        holder.setText(R.id.tv_title, ((L1) data).getL1str());
                        break;
                    case 2:
                        holder.setText(R.id.tv_title, ((L3) data).getStr());
                        break;
                }
            }
        };
        mRvBase.setAdapter(expandableAdapter);
        expandableAdapter.expandAll();
    }

}
