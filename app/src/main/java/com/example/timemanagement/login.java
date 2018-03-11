package com.example.timemanagement;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timemanagement.SQLiteDataBase.UserDatabaseHelper;


public class login extends Activity {
	Cursor cursor;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		UserDatabaseHelper dbh=new UserDatabaseHelper(this,"SWX.db",null,1);
		final SQLiteDatabase sqldb=dbh.getWritableDatabase();
		final Button register=(Button) findViewById(R.id.register);
		final Button sure=(Button) findViewById(R.id.sure);
		final TextView login=(TextView)findViewById(R.id.login);
		final EditText username_input=(EditText)findViewById(R.id.username_input);
		final EditText password_input=(EditText)findViewById(R.id.password_input);
		final ContentValues temp=new ContentValues();


		intent=new Intent(login.this,HomePage.class);
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				register.setText("退出");
				login.setText("注册");
				sure.setText("注册");
				username_input.setEnabled(false);
				password_input.setText("");
				intent.putExtra("featherNumber", 0);
				cursor=sqldb.query("User",new String[]{"id"}, null, null, null, null, "id");
				if(cursor.getCount()==0)
				{
					username_input.setText("1000");
					temp.put("id", 1000);
					intent.putExtra("username", "1000");
				}
				else
				{
					cursor.moveToLast();
					int k=cursor.getInt(cursor.getColumnIndex("id"))+1;
					username_input.setText(String.valueOf(k));
					intent.putExtra("username",String.valueOf(k));
					temp.put("id", k);
				}
				v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						login.this.finish();
					}
				});
				sure.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if(password_input.getText().length()!=0)
						{
							temp.put("password", password_input.getText().toString());
							sqldb.insert("User", null, temp);
							cursor.close();
							Toast.makeText(login.this, "注册成功", Toast.LENGTH_SHORT).show();
							startActivity(intent);
							login.this.finish();
						}
						else
							Toast.makeText(login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(username_input.getText().length()!=0&&password_input.getText().length()!=0){
					cursor=sqldb.query("User", null, "id=?",new String[]{username_input.getText().toString()}, null, null, null);
					if(cursor.getCount()==0)
					{
						Toast.makeText(login.this, "用户名不存在", Toast.LENGTH_SHORT).show();
					}
					else
					{
						cursor.moveToFirst();
						String a=cursor.getString(cursor.getColumnIndex("password"));
						String b=password_input.getText().toString();
						intent.putExtra("featherNumber",cursor.getColumnIndex("feather"));

						if(a.equals(b))
						{
							//Toast.makeText(login.this, "密码正确", Toast.LENGTH_SHORT).show();
							intent.putExtra("username",username_input.getText().toString());
							startActivity(intent);
						}
						else
						{
							Toast.makeText(login.this, "密码错误", Toast.LENGTH_SHORT).show();
						}
					}
				}
				else if(username_input.getText().length()==0)
					Toast.makeText(login.this, "请输入用户名", Toast.LENGTH_SHORT).show();
				else if(password_input.getText().length()==0)
					Toast.makeText(login.this, "请输入密码", Toast.LENGTH_SHORT).show();
			}

		});


	}
}