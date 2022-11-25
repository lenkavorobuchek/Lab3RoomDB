package com.example.myroomapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<User> users;
    List<Contacts> contacts;
    List<Other> others;
    EditText idField, columnsField;
    UserDao userDao;
    ContactsDao contDao;
    OtherDao otherDao;
    Button fill, clear;
    ActivityResultLauncher<Intent> ActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //to disable the app name on all windows
        //getSupportActionBar().hide();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        userDao = db.userDao();
        contDao = db.contactsDao();
        otherDao = db.otherDao();

        idField = findViewById(R.id.idField);
        columnsField = findViewById(R.id.columnsField);
        clear = findViewById(R.id.clear_switch);
        fill = findViewById(R.id.fill_switch);

        users = userDao.getAll();
        contacts = contDao.getAll();
        others = otherDao.getAll();

        RecyclerView recView = (RecyclerView) findViewById(R.id.recycler);
        StateAdapter adapter = new StateAdapter(this, users, contacts, others);
        recView.setAdapter(adapter);

        clear.setOnClickListener(view -> {
            userDao.deleteAll();
            contDao.deleteAll();
            otherDao.deleteAll();
            users.clear();
            contacts.clear();
            others.clear();
            adapter.setNewStates(users, contacts, others);
            adapter.notifyDataSetChanged();
        });

        fill.setOnClickListener(view -> {
            User a = new User("Helen", "Vorobeva", "Ivanovna");
            User b = new User("Misha", "Vorobev", "Alexandrovich");
            User c = new User("Andrew", "Ivanov", "Nikolaevich");
            User d = new User("Kolay", "Romanov", "Alexandrovich");

            Contacts ca = new Contacts("89506070801", "lenkavorobuchek@mail.ru");
            Contacts cb = new Contacts("8(930)703-07-30", "vorobev@gmail.com");
            Contacts cc = new Contacts("89999999999", "name@mail.com");
            Contacts cd = new Contacts("8(950)9995665", "romanov@ya.ru");

            Other oa = new Other("HSE", "School");
            Other ob = new Other("HSE", "Epam");
            Other oc = new Other("UNN", "Yandex");
            Other od = new Other("MGU", "Google");

            userDao.insertAll(a, b, c, d);
            contDao.insertAll(ca, cb, cc, cd);
            otherDao.insertAll(oa, ob, oc, od);
            users.addAll(Arrays.asList(a,b,c,d));
            contacts.addAll(Arrays.asList(ca, cb, cc, cd));
            others.addAll(Arrays.asList(oa, ob, oc, od));
            adapter.setNewStates(users, contacts, others);
            adapter.notifyDataSetChanged();
        });

        columnsField.setOnFocusChangeListener((view, isInFocus) -> {
            if (!isInFocus) {
                String column_str = columnsField.getText().toString();

                String pattern = "([A-Za-z_]+, ?)*([A-Za-z_]+)|\\*";
                boolean are_really_exist = false;
                if (column_str.equals("*")) {
                    are_really_exist = true;
                }
                String dynamic_column_query, dynamic_receive_data_query;
                if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    dynamic_column_query = "SELECT * FROM user, contacts, other WHERE user.uid LIKE contacts.uid AND user.uid LIKE other.uid AND contacts.uid LIKE other.uid";
                    dynamic_receive_data_query = "SELECT " + column_str + " FROM user, contacts, other WHERE user.uid LIKE contacts.uid AND user.uid LIKE other.uid AND contacts.uid LIKE other.uid";
                } else {
                    dynamic_column_query = "SELECT * FROM user";
                    dynamic_receive_data_query = "SELECT " + column_str + " FROM user";
                }
                String[] field_names = db.query(new SimpleSQLiteQuery(dynamic_column_query)).getColumnNames();
                if (!are_really_exist) {
                    for (String s : column_str.split(",")) {
                        are_really_exist = false;
                        for (String db_field_name : field_names) {
                            if (s.trim().equals(db_field_name)) {
                                are_really_exist = true;
                                break;
                            }
                        }
                        if (!are_really_exist) {
                            break;
                        }
                    }
                }

                if (column_str.matches(pattern) && are_really_exist) {
                    SupportSQLiteQuery query = new SimpleSQLiteQuery(dynamic_receive_data_query);
                    users = userDao.getAllByColumns(query);
                    contacts = contDao.getAllByColumns(query);
                    others = otherDao.getAllByColumns(query);
                    adapter.setNewStates(users, contacts, others);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Знак *, один или несколько столбцов через запятую: " + columnsField.getHint(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //startActivityForResult and onActivityResult now deprecated! new way below
        ActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result) -> {
            assert result.getData() != null;
            Bundle results = result.getData().getExtras();
            User newUser = new User(results.getString("name"), results.getString("surname"), results.getString("patronymic"));
            Contacts receivedContacts = null;
            Other receivedPlaces = null;
            if (results.getString("study") != null) {
                receivedContacts = new Contacts(results.getString("phone"), results.getString("email"));
                receivedPlaces = new Other(results.getString("study"), results.getString("work"));
            } else {
                receivedContacts = new Contacts("----------------------", "------------------");
                receivedPlaces = new Other("------", "----------");
            }
            switch (result.getResultCode()) {
                case 1:
                    if (results.getString("study") != null) {
                        contDao.insert(receivedContacts);
                        otherDao.insert(receivedPlaces);
                        contacts.add(receivedContacts);
                        others.add(receivedPlaces);
                    } else {
                        Contacts newContacts = new Contacts("----------------------", "------------------");
                        Other newPlaces = new Other("------", "----------");
                        contDao.insert(newContacts);
                        otherDao.insert(newPlaces);
                        contacts.add(newContacts);
                        others.add(newPlaces);
                    }
                    userDao.insert(newUser);
                    users.add(newUser);
                    adapter.notifyItemInserted(users.size() - 1);
                    break;
                case 2:
                    if (results.getString("study") != null && receivedContacts != null) {
                        receivedContacts.uid = results.getInt("id");
                        receivedPlaces.uid = results.getInt("id");
                    }
                    newUser.uid = results.getInt("id");
                    boolean only_view = results.getBoolean("view");
                    if (!only_view) {
                        userDao.update(newUser);
                        contDao.update(receivedContacts);
                        otherDao.update(receivedPlaces);
                    }
                    users.set(results.getInt("position"), newUser);
                    contacts.set(results.getInt("position"), receivedContacts);
                    others.set(results.getInt("position"), receivedPlaces);
                    adapter.notifyItemChanged(results.getInt("position"));
                    break;
            }
        });

        adapter.setOnClickListener(view -> {
            int id = view.getId();
            User modifiedUser = adapter.getKthUser(id);
            Contacts chosenContacts = adapter.getKthContacts(id);
            Other chosenOtherInfo = adapter.getKthOtherInfo(id);
            if (modifiedUser == null || chosenContacts == null || chosenOtherInfo == null) {
                Toast.makeText(getApplicationContext(), "Нет такого пользователя", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, FillActivity.class);
            intent.putExtra("modified user", modifiedUser);
            intent.putExtra("modified contacts", chosenContacts);
            intent.putExtra("modified other info", chosenOtherInfo);
            boolean only_view = modifiedUser.hasZeroFields();
            if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                only_view = only_view || chosenContacts.hasZeroFields() || chosenOtherInfo.hasZeroFields();
            }
            intent.putExtra("position", id);
            intent.putExtra("view", only_view);
            ActivityResultLauncher.launch(intent);
            });


        Button create = findViewById(R.id.create_button);
        create.setOnClickListener(view -> {
            Intent intent = new Intent(this, FillActivity.class);
            ActivityResultLauncher.launch(intent);
        });
        Button edit = findViewById(R.id.change_button);
        edit.setOnClickListener(view -> {
            String idString = idField.getText().toString();
            String pattern = "[0-9]+";
            Intent intent = new Intent(this, FillActivity.class);

            boolean only_view = false;
            if (idString.matches(pattern) && Integer.parseInt(idString) < users.size()) {
                int id;
                if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (Integer.parseInt(idString) >= contacts.size() && Integer.parseInt(idString) >= others.size()) {
                        Toast.makeText(getApplicationContext(), "Полe Позиция не соответствует нужному формату", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        id = Integer.parseInt(idString);
                    }
                }
                id = Integer.parseInt(idString);
                User modifiedUser = userDao.getByPosition(id);
                Contacts modifiedContacts = contDao.getByPosition(id);
                Other modifiedPlaces = otherDao.getByPosition(id);
                if (modifiedContacts == null || modifiedPlaces == null || modifiedUser == null) {
                    Toast.makeText(getApplicationContext(), "Нет такого пользователя", Toast.LENGTH_SHORT).show();
                    return;
                }
                only_view = modifiedUser.hasZeroFields() || modifiedContacts.hasZeroFields() || modifiedPlaces.hasZeroFields();
                intent.putExtra("modified contacts", modifiedContacts);
                intent.putExtra("modified other info", modifiedPlaces);
                intent.putExtra("modified user", modifiedUser);
                intent.putExtra("position", id);
                intent.putExtra("view", only_view);

                ActivityResultLauncher.launch(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Полe Позиция не соответствует нужному формату", Toast.LENGTH_SHORT).show();
            }
        });
        Button delete = findViewById(R.id.delete_button);
        delete.setOnClickListener(view -> {
            String idString = idField.getText().toString();
            String pattern = "[0-9]+";
            int id;
            if (idString.matches(pattern) && Integer.parseInt(idString) < users.size()) {
                if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (Integer.parseInt(idString) >= contacts.size() && Integer.parseInt(idString) >= others.size()) {
                        Toast.makeText(getApplicationContext(), "Полe Позиция не соответствует нужному формату", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    id = Integer.parseInt(idString);
                    contacts.remove(id);
                    others.remove(id);
                }
                id = Integer.parseInt(idString);
                userDao.delete(id);
                contDao.delete(id);
                otherDao.delete(id);
                users.remove(id);
                adapter.notifyItemRemoved(id);
            } else {
                if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (contacts.isEmpty() || others.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "База данных пользователей пуста, удаление невозможно", Toast.LENGTH_SHORT).show();
                    }
                }
                if (users.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "База данных пользователей пуста, удаление невозможно", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Полe Позиция не соответствует нужному формату", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}