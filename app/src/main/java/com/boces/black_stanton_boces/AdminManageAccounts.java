package com.boces.black_stanton_boces;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.AdminAccount;

import java.util.List;

public class AdminManageAccounts extends AppCompatActivity {

    private RecyclerView accountList;
    private AdminAccountAdapter accountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_accounts);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddAccount);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminAddAccountActivity.class));
            }
        });


        PersistenceInteractor persistence = new PersistenceInteractor(this);
        accountAdapter = new AdminAccountAdapter(persistence.getAllAdminAccounts());
        accountList = findViewById(R.id.accountList);
        accountList.setAdapter(accountAdapter);
        accountList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        accountAdapter.setAccounts(persistence.getAllAdminAccounts());
        accountAdapter.notifyDataSetChanged();
    }

    private class AdminAccountAdapter extends RecyclerView.Adapter<AdminAccountAdapter.ViewHolder>{
        List<AdminAccount> accounts;

        public AdminAccountAdapter(List<AdminAccount> accounts) {
            this.accounts = accounts;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View taskView = inflater.inflate(R.layout.item_account, parent, false);
            return new ViewHolder(taskView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AdminAccount adminAccount = accounts.get(position);

            holder.accountId = adminAccount.getId();
            holder.username.setText(adminAccount.getUsername());
        }

        @Override
        public int getItemCount() {
            return accounts.size();
        }

        public List<AdminAccount> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<AdminAccount> accounts) {
            this.accounts = accounts;
        }

        @SuppressWarnings("WeakerAccess")
        public class ViewHolder extends RecyclerView.ViewHolder {
            public int accountId;
            public TextView username;

            public ViewHolder(View v) {
                super(v);
                username = v.findViewById(R.id.lblAdminUsername);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (accountId < 1)
                            throw new IllegalStateException("User Id Not Defined");
                        Intent editAccount = new Intent(getApplicationContext(), AdminEditAccountActivity.class);
                        editAccount.putExtra(AdminEditAccountActivity.BUNDLE_KEY.ACCOUNT_ID.name(), accountId);
                        startActivity(editAccount);
                    }
                });
            }
        }
    }
}
