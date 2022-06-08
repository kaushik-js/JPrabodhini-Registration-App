package kjxs.android.jpregistration;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText nm,ph;
    ListView list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> aad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nm = findViewById(R.id.nm);
        ph = findViewById(R.id.phn);
        list = findViewById(R.id.list);
        aad = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listItems);
        list.setAdapter(aad);
        db.collection("phonelogin").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    int cnt=1;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        listItems.add(cnt+". "+document.getString("name")+" :- "+document.getString("phone"));
                        aad.notifyDataSetChanged();
                        cnt++;
                    }
                }
            }
        });
    }
    public void saved(View view)
    {
        String unm = nm.getText().toString().trim();
        String upn = ph.getText().toString().trim();
        Map<String, Object> user = new HashMap<>();
        user.put("name", unm);
        user.put("phone", upn);
        db.collection("phonelogin").document(upn).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "SAVED", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void deleted(View view)
    {
        String upn = ph.getText().toString().trim();
        db.collection("phonelogin").document(upn).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "DELETED", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
