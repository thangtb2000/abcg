package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editidUser,editfirstName,editlastName, editemail;
    Button btnThem,btnSua,btnXoa;
    Adapter adt;
    ListView listView;
    TextView txtId, txtFirstName, txtLastName, txtEmail;
    ArrayList<User> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editidUser = findViewById(R.id.editID);
        editfirstName = findViewById(R.id.editFN);
        editlastName = findViewById(R.id.editLN);
        editemail = findViewById(R.id.editEmail);

        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);

        txtId = findViewById(R.id.id);
        txtFirstName = findViewById(R.id.firstName);
        txtLastName = findViewById(R.id.lastName);
        txtEmail = findViewById(R.id.Email);

        listView = findViewById(R.id.lst_View);

        String urlGet = "https://60b4e0e1fe923b0017c82ed6.mockapi.io/Nguoidung";
        GetData(urlGet);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = arrayList.get(position);
                id = user.getId();
                String firstName = user.getFirstName();
                String lastName = user.getLastName();
                String email = user.getEmail();
                System.setProperty("ID", id+"");
                System.setProperty("FirstName",firstName);
                System.setProperty("LastName",lastName);
                System.setProperty("Email",email);
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {//????a s??? ki???n v?? n??t
            @Override
            public void onClick(View v) {
                String urlDel="https://60b4e0e1fe923b0017c82ed6.mockapi.io/Nguoidung";//nh??? ch??? n??y k c?? / nhaok
                PostApi(urlDel);//s??? ki???n n??y vi???t ri??ng h??m
                GetData(urlGet);
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlDel="https://60b4e0e1fe923b0017c82ed6.mockapi.io/Nguoidung";
                PutApi(urlDel);
                GetData(urlGet);
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlDel="https://60b4e0e1fe923b0017c82ed6.mockapi.io/Nguoidung";
                DeleteAPI(urlDel);
                GetData(urlGet);
            }
        });


    }

    private void GetData(String urlGet) {
        arrayList = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                arrayList.add(new User(object.getInt("id"),
                                        object.getString("firstName"),
                                        object.getString("lastName"),
                                        object.getString("email")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adt = new Adapter(getApplicationContext(), arrayList, R.layout.item);
                        listView.setAdapter(adt);
                        adt.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Loi", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void DeleteAPI(String url) { // 3 h??m n?? n?? oky chang nhau c??? ?? kh??c nhau ch??? n??y n??
        StringRequest request = new StringRequest(
                Request.Method.DELETE, url+'/'+editidUser.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "Successfully Delete", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Faild Delete", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void PutApi(String url) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, url + "/" + editidUser.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error by Put data!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {//v?? ????y l?? s???a n??n m??nh ph???i g???i th??m m???y c??i trong editext ra????? s???a ??
                HashMap<String, String> params = new HashMap<>();
                params.put("email", editemail.getText().toString());
                params.put("lastName", editfirstName.getText().toString());
                params.put("firstName", editlastName.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void PostApi(String url) {
        StringRequest stringRequest = new StringRequest( Request.Method.POST, // th??m th?? id t??? cho v??o n??n k c???n add id ??, n??n kh??c ch??? n??y n???a, c???n th???n nh???m
                url+"",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error by Post data!", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {//th??m c??ng c???n d??? li???u n??n c?? ??o???n n??y nha
                HashMap<String, String> params = new HashMap<>();
                params.put("email", editemail.getText().toString());
                params.put("lastName", editfirstName.getText().toString());
                params.put("firstName", editlastName.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}