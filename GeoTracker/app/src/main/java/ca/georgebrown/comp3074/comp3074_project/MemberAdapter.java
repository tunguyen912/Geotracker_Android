package ca.georgebrown.comp3074.comp3074_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MemberAdapter extends ArrayAdapter<Member> {
    private int layout;
    public MemberAdapter(@NonNull Context context, int resource, @NonNull Member[] objects) {
        super(context, resource, objects);
        layout = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView ==null){
            LayoutInflater inflater =LayoutInflater.from(getContext());
            convertView =inflater.inflate(layout, null);
        }
        Member member = getItem(position);
        TextView name = convertView.findViewById(R.id.name);
        TextView stuId = convertView.findViewById(R.id.stuId);
        name.setText(member.getName());
        stuId.setText(member.getStuId()+"");
        return convertView;
    }
}
