package licancan.com.recyclerviewcar;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import licancan.com.recyclerviewcar.adapter.ShopCartAdapter;
import licancan.com.recyclerviewcar.api.Api;
import licancan.com.recyclerviewcar.bean.ShopCartBean;
import licancan.com.recyclerviewcar.utils.OkHttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView jiesuan,allselect,tv_Allnum;
    private RecyclerView recyclerView;
    private ShopCartAdapter adapter;
    private List<ShopCartBean.CartlistBean> mAllOrderList = new ArrayList<>();
    private ArrayList<ShopCartBean.CartlistBean> mGoPayList = new ArrayList<>();
    private List<String> mHotProductsList = new ArrayList<>();
    private TextView CarAllPrice;
    private int mCount,mPosition;
    private float allPrice;
    private boolean mSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initMyData();

        //创建LinearLayoutManager管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //创建适配器  关联适配器
        adapter = new ShopCartAdapter(this,mAllOrderList);
        recyclerView.setAdapter(adapter);
        //删除商品接口
        adapter.setOnDeleteClickListener(new ShopCartAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position,int cartid) {
                adapter.notifyDataSetChanged();
            }
        });
        //修改数量接口
        adapter.setOnEditClickListener(new ShopCartAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(int position, int cartid, int count) {
                mCount = count;
                mPosition = position;
            }
        });
        //判断全选按钮
        adapter.setResfreshListener(new ShopCartAdapter.OnResfreshListener() {
            @Override
            public void onResfresh( boolean isSelect) {
                mSelect = isSelect;
                if(isSelect){
                    Drawable left = getResources().getDrawable(R.drawable.shopcart_selected);
                    allselect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                }else {
                    Drawable left = getResources().getDrawable(R.drawable.shopcart_unselected);
                    allselect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                }
                float allPrice = 0;
                int allNum = 0;
                allPrice = 0;
                mGoPayList.clear();
                for(int i = 0;i < mAllOrderList.size(); i++)
                    if(mAllOrderList.get(i).getIsSelect()) {
                        allPrice += Float.parseFloat(mAllOrderList.get(i).getPrice()) * mAllOrderList.get(i).getCount();
                        allNum += 1;
                        mGoPayList.add(mAllOrderList.get(i));
                    }
                allPrice = allPrice;
                CarAllPrice.setText("总价：" + allPrice);
                tv_Allnum.setText("共" + allNum + "件商品");
                jiesuan.setText("结算"+"("+allNum+")");
            }
        });

        //全选
        allselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelect = !mSelect;
                if(mSelect){
                    Drawable left = getResources().getDrawable(R.drawable.shopcart_selected);
                    allselect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                    for(int i = 0;i < mAllOrderList.size();i++){
                        mAllOrderList.get(i).setSelect(true);
                        mAllOrderList.get(i).setShopSelect(true);
                    }
                }else{
                    Drawable left = getResources().getDrawable(R.drawable.shopcart_unselected);
                    allselect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                    for(int i = 0;i < mAllOrderList.size();i++){
                        mAllOrderList.get(i).setSelect(false);
                        mAllOrderList.get(i).setShopSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        allselect = (TextView) findViewById(R.id.allselect);
        CarAllPrice = (TextView) findViewById(R.id.tv_shopcart_totalprice);
        tv_Allnum = (TextView) findViewById(R.id.tv_Allnum);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        jiesuan = (TextView) findViewById(R.id.jiesuan);
    }

    private void initData() {
        OkHttpUtils.doGet(Api.URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

            }
        });
}
    public static void isSelectFirst(List<ShopCartBean.CartlistBean> list){
        if(list.size() > 0) {
            list.get(0).setIsFirst(1);
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).getShopId() == list.get(i - 1).getShopId()) {
                    list.get(i).setIsFirst(2);
                } else {
                    list.get(i).setIsFirst(1);
                }
            }
        }
}
























































    private void initMyData(){
        for(int i = 0;i < 2;i ++){
            ShopCartBean.CartlistBean sb = new ShopCartBean.CartlistBean();
            sb.setShopId(1);
            sb.setPrice("5199.0");
            sb.setDefaultPic("https://m.360buyimg.com/n0/jfs/t6700/155/2098998076/156185/6cf95035/595dd5a5Nc3a7dab5.jpg");
            sb.setProductName("Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机");
            sb.setShopName("商家1");
            sb.setCount(1);
            mAllOrderList.add(sb);
        }

        for(int i = 0;i < 1;i ++){
            ShopCartBean.CartlistBean sb = new ShopCartBean.CartlistBean();
            sb.setShopId(2);
            sb.setPrice("272.0");
            sb.setDefaultPic("https://m.360buyimg.com/n0/jfs/t9004/210/1160833155/647627/ad6be059/59b4f4e1N9a2b1532.jpg");
            sb.setProductName("北京稻香村 稻香村中秋节月饼 老北京月饼礼盒655g");
            sb.setShopName("商家6");
            sb.setCount(1);
            mAllOrderList.add(sb);
        }

        for(int i = 0;i < 2;i ++){
            ShopCartBean.CartlistBean sb = new ShopCartBean.CartlistBean();
            sb.setShopId(3);
            sb.setPrice("2434.0");
            sb.setDefaultPic("https://m.360buyimg.com/n0/jfs/t9106/106/1785172479/537280/253bc0ab/59bf78a7N057e5ff7.jpg");
            sb.setProductName("小米（MI） 小米MIX2 手机 黑色 全网通 (6GB+64GB)【标配版】");
            sb.setShopName("商家8");
            sb.setCount(1);
            mAllOrderList.add(sb);
        }

        for(int i = 0;i < 1;i ++){
            ShopCartBean.CartlistBean sb = new ShopCartBean.CartlistBean();
            sb.setShopId(4);
            sb.setPrice("4362.0");
            sb.setDefaultPic("https://m.360buyimg.com/n0/jfs/t6130/97/1370670410/180682/1109582a/593276b1Nd81fe723.jpg");
            sb.setProductName("全球购 新款Apple MacBook Pro 苹果笔记本电脑 银色VP2新13英寸Bar i5/8G/256G");
            sb.setShopName("商家10");
            sb.setCount(1);
            mAllOrderList.add(sb);
        }

        /*OkHttpClient ok = new OkHttpClient.Builder().build();
        FormBody.Builder fb = new FormBody.Builder();
        fb.add("uid","148");
        FormBody build = fb.build();
        Request re = new Request.Builder().post(build).url("http://120.27.23.105/product/getCarts")
                .build();
        ok.newCall(re).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                System.out.println("string = " + string);
                try {

                    JSONObject json = new JSONObject(string);
                    JSONArray data = json.getJSONArray("data");

                    for (int i = 0; i <data.length() ; i++) {
                        ShopCartBean.CartlistBean sb = new ShopCartBean.CartlistBean();
                        JSONObject o = (JSONObject) data.get(i);
                        String name = o.optString("sellerName");
                        int sellerid = o.optInt("sellerid");
                        sb.setShopId(sellerid);
                        sb.setShopName(name);
                        JSONArray list = o.optJSONArray("list");
                        for (int j = 0; j <list.length() ; j++) {
                            JSONObject o1 = (JSONObject) list.get(j);
                            String images = o1.getString("images").split("\\|")[0];
                            String title = o1.optString("title");
                            String price = o1.optString("price");
                            sb.setDefaultPic(images);
                            sb.setProductName(title);
                            sb.setPrice(price);
                            System.out.println("title = +======" + title);
                            mAllOrderList.add(sb);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });*/
        isSelectFirst(mAllOrderList);
    }


}







































































