package cn.edu.gdmec.s07150843.listviewdemo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listview);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "ArrayAdapter");
        menu.add(0, 2, 0, "SimpleCursorAdapter");
        menu.add(0, 3, 0, "SimpleAdapter");
        menu.add(0, 4, 0, "BaseAdater");
        menu.add(0, 5, 0, "退出");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case 1:
                arrayAdater();
                break;
            case 2:
                simpleCursorAdapter();
                break;
            case 3:
                simpleAdapter();
                break;
            case 4:
                baseAdater();
                break;
            case 5:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void arrayAdater() {
        //准备资源数组。
        final String time[] = {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, time);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, time[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void simpleCursorAdapter() {
        /*
        getContentResolver().query()的方法实现去掉重复的数据
        Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)；
   /    根据URI对象ContactsContract.Contacts.CONTENT_URI查询所有联系人；
         */
        final Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        startManagingCursor(cursor);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_1, cursor, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, new int[]{android.R.id.text1}, 0);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "s", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void simpleAdapter() {

        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("title", "G1");

        map.put("info", "google1");

        map.put("img", android.R.mipmap.sym_def_app_icon);

        list.add(map);

        map = new HashMap<String, Object>();

        map.put("title", "G2");

        map.put("info", "google2");

        map.put("img", android.R.mipmap.sym_def_app_icon);

        list.add(map);

        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.simpleadapter_demo, new String[]{"img", "title", "info"}, new int[]{R.id.imageView, R.id.titleView, R.id.infoView});

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, list.get(position).get("title").toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    static class ViewHolder {
        public ImageView img;
        public TextView info, title;
        public Button btn;
        public LinearLayout layout;
    }
    private void baseAdater(){
        class MyBaseAdater extends BaseAdapter {
            private List<Map<String, Object>> date;
            private Context context;
            private LayoutInflater myLayoutInflater;
            public MyBaseAdater(Context context, List<Map<String, Object>> date) {
                super();

                this.context = context;
                this.date = date;
               /*
               LayoutInflater作用类似于findViewByid()；不同点是layoutInflater是用来找res/layout/下的xml文件，并实例化.
               findViewByid是用来布局文件下具体的埪件
               1、对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；
               2、对于一个已经载入的界面，就可以使用Activiyt.findViewById()方法来获得其中的界面元素。
               获得 LayoutInflater 实例的三种方式:
               1.LayoutInflater inflater = getLayoutInflater();  //调用Activity的getLayoutInflater()
               2.LayoutInflater localinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                3. LayoutInflater inflater = LayoutInflater.from(context);
                */
                this.myLayoutInflater = LayoutInflater.from(context);
            }

            @Override
            public int getCount() {
                return date.size();
            }

            @Override
            public Object getItem(int position) {
                return date.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
           /*getView 的三个参数各代表什么？
          1、 int position      该属性是判断显示在界面上的是第几个.
          2、 View convertView   convertView代表一个item,当数据过多时，划出屏幕范围的数据暂时会被进入屏幕中的数据替换。
           滑出屏幕的converView就在下面新进来的item中重新使用，只是修改下他展示的值。简单介绍是，资源的重新使用。
          3、ViewGroup parent     这个属性是加载xml视图时使用。
inflate       (R.layout.adapter__item, parent, false);确定他父控件，减少宽高的测算
           */
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder=null;
                if(convertView==null){
                   /*
                   对holder进行实例化，并对该类的属性惊醒赋值。
                    */
                    holder=new ViewHolder();
                    convertView=myLayoutInflater.inflate(R.layout.l1,parent,false);
                    holder.img= (ImageView) convertView.findViewById(R.id.img);
                    holder.title= (TextView) convertView.findViewById(R.id.title);
                    holder.info= (TextView) convertView.findViewById(R.id.info);
                    holder.btn= (Button) convertView.findViewById(R.id.btn);
                    holder.layout= (LinearLayout) convertView.findViewById(R.id.l1);
                    //将资源加入到convertView视图控件里面。
                    convertView.setTag(holder);
                }else{
                    holder= (ViewHolder) convertView.getTag();
                }
                holder.img.setImageResource(Integer.parseInt(date.get(position).get("img").toString()));

                holder.title.setText(date.get(position).get("title").toString());

                holder.info.setText(date.get(position).get("info").toString());

                if(position%2==1){
                    holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));

                }else{
                    holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                }
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"点击"+(position+1),Toast.LENGTH_SHORT).show();
                    }
                });
                return convertView;

            }

        }
        final List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();

        Map<String,Object> map=new HashMap<String,Object>();

        map.put("title","G1");
        map.put("info","goole 1");
        map.put("img",R.mipmap.ic_launcher);
        list.add(map);

        map=new HashMap<String ,Object>();

        map.put("title","G2");
        map.put("info","G2");
        map.put("img",R.mipmap.ic_launcher);
        list.add(map);

        map=new HashMap<String ,Object>();

        map.put("title","G3");
        map.put("info","goole3");
        map.put("img",R.mipmap.ic_launcher);

        list.add(map);

        MyBaseAdater myBaseAdater=new MyBaseAdater(this,list);

        lv.setAdapter(myBaseAdater);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,list.get(position).get("title").toString(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}
