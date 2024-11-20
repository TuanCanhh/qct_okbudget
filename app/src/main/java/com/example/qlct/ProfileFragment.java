package com.example.qlct;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qlct.Editprofile;
import com.example.qlct.Login;
import com.example.qlct.R;
import com.example.qlct.SignUp;

public class ProfileFragment extends Fragment {

    // Khai báo các biến
     TextView editProfile, changePass;
     Button logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Ánh xạ các biến với các thành phần trong layout
        editProfile = view.findViewById(R.id.edit_profile);
        changePass = view.findViewById(R.id.change_pass);
        logout = view.findViewById(R.id.logout);

        // Thiết lập sự kiện onClick cho editProfile
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Editprofile.class);
                startActivity(intent);
            }
        });

//        // Thiết lập sự kiện onClick cho changePass (nếu cần)
//        changePass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), asd   );
//                startActivity(intent);
//            }
//        });

        // Thiết lập sự kiện onClick cho logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish(); // Kết thúc activity hiện tại
            }
        });

        return view;
    }
}
