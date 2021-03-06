package com.example.computermanage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.computermanage.dao.DAOHoaDon;
import com.example.computermanage.dao.DAOHoaDonCT;
import com.example.computermanage.dao.DAOSanPham;
import com.example.computermanage.model.GetSLNhap;
import com.example.computermanage.model.Hang;
import com.example.computermanage.model.HoaDon;
import com.example.computermanage.model.HoaDonChiTiet;
import com.example.computermanage.R;
import com.example.computermanage.model.SanPham;
import com.example.computermanage.ui.hoadonnhap.ActivityChiTietHoaDonNhap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class AdapterHDNhap extends RecyclerView.Adapter<AdapterHDNhap.HDNhapViewHolder> implements Filterable {
    private Context context;
    ArrayList<HoaDon> listHDNhap;
    ArrayList<HoaDon> listHDNhapOld;
    ArrayList<HoaDonChiTiet> listHDCT;
    DAOHoaDon daoHoaDon;
    DAOSanPham daoSanPham;
    DAOHoaDonCT daoHoaDonCT;

    public AdapterHDNhap(Context context, ArrayList<HoaDon> listHDNhap) {
        this.context = context;
        this.listHDNhap = listHDNhap;
        this.listHDNhapOld = listHDNhap;
        daoHoaDon = new DAOHoaDon(context);
        daoSanPham = new DAOSanPham(context);
        daoHoaDonCT = new DAOHoaDonCT(context);

    }

    @NonNull
    @Override
    public HDNhapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoadonnhap, parent, false);
        return new HDNhapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHDNhap.HDNhapViewHolder holder, int position) {
        HoaDon hoaDon = listHDNhap.get(position);
        if (hoaDon == null) {
            return;
        }
        holder.tv_maHDNhap.setText("M?? H??: " + hoaDon.getMshd());
        holder.tv_ngayHDNhap.setText("Ng??y: " + hoaDon.getNgaymua());
        HoaDonChiTiet hoaDonChiTiet = daoHoaDonCT.getMaHD(hoaDon.getMshd());
        String maSP = hoaDonChiTiet.getMssp();
        SanPham sanPham = daoSanPham.getID(hoaDonChiTiet.getMssp());

        String tensp = daoSanPham.getID(maSP).getTensp();
        holder.tv_tenHDSP.setText("S???n ph???m: " + tensp);
        int tinhtrangsp = daoSanPham.getID(maSP).getTinhtrang();
        if (daoSanPham.getID(maSP).getHinhanh() == null) {
            TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect(tensp.substring(0, 1).toUpperCase(), getRandomColor());
            holder.img_viewAnhHDNhap.setImageDrawable(textDrawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(daoSanPham.getID(maSP).getHinhanh(), 0, daoSanPham.getID(maSP).getHinhanh().length);
            holder.img_viewAnhHDNhap.setImageBitmap(bitmap);

        }
        switch (tinhtrangsp) {
            case 0:
                holder.tv_tinhtrangHDNhap.setText("T??nh tr???ng: C??");
                break;
            case 1:
                holder.tv_tinhtrangHDNhap.setText("T??nh tr???ng: Like new 99%");
                break;
            case 2:
                holder.tv_tinhtrangHDNhap.setText("T??nh tr???ng: M???i");
                break;
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tv_dongiaHDNhap.setText("Gi??: " + decimalFormat.format(hoaDonChiTiet.getDongia()) + "??");
        holder.tv_soluongHDNhap.setText("S??? l?????ng: " + hoaDonChiTiet.getSoluong());
        double thanhtien = hoaDonChiTiet.getSoluong() * hoaDonChiTiet.getDongia();
        holder.tv_thanhtienHDNhap.setText("Th??nh ti???n: " + decimalFormat.format(thanhtien) + "??");
        holder.img_deleteHDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_delete, null);
                builder1.setView(view1);
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog1.show();
                Button button = view1.findViewById(R.id.btn_delete);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int kq = daoHoaDon.deleteHoaDon(String.valueOf(hoaDon.getMshd()));
                        int kq1 = daoHoaDonCT.deleteHoaDonCT(hoaDonChiTiet.getMshdct());
                        if (kq > 0 && kq1 > 0) {
                            listHDNhap.clear();
                            listHDNhap.addAll(daoHoaDon.getAllNhap());
                            notifyDataSetChanged();
                            Toast.makeText(view1.getContext(), "X??a th??nh c??ng ", Toast.LENGTH_SHORT).show();
                            sanPham.setTrangthai(1);
                            daoSanPham.updateSanPham(sanPham);
                            alertDialog1.dismiss();
                        } else {
                            Toast.makeText(view1.getContext(), "X??a th???t b???i ", Toast.LENGTH_SHORT).show();
                            alertDialog1.dismiss();
                        }
                    }


                });
            }
        });

        holder.cv_chitietHDNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChiTietHoaDonNhap.class);
                intent.putExtra("mahd", hoaDon.getMshd());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listHDNhap.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty()) {
                    listHDNhap = listHDNhapOld;
                } else {
                    ArrayList<HoaDon> list = new ArrayList<>();
                    for (HoaDon hoaDon : listHDNhap) {
                        HoaDonChiTiet hoaDonChiTiet = daoHoaDonCT.getID(hoaDon.getMshd());
                        SanPham sanPham = daoSanPham.getID(hoaDonChiTiet.getMssp());
                        if (hoaDon.getMshd().contains(search)) {
                            list.add(hoaDon);
                        } else if (sanPham.getTensp().toLowerCase().contains(search.toLowerCase())) {
                            list.add(hoaDon);
                        }
                    }
                    listHDNhap = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listHDNhap;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listHDNhap = (ArrayList<HoaDon>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class HDNhapViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenHDSP;
        TextView tv_maHDNhap, tv_tinhtrangHDNhap, tv_ngayHDNhap, tv_soluongHDNhap, tv_dongiaHDNhap, tv_thanhtienHDNhap;
        ImageView img_viewAnhHDNhap, img_deleteHDN;
        CardView cv_chitietHDNhap;

        public HDNhapViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tenHDSP = itemView.findViewById(R.id.tv_tenHDSP);
            tv_maHDNhap = itemView.findViewById(R.id.tv_maHDNhap);
            tv_tinhtrangHDNhap = itemView.findViewById(R.id.tv_tinhtrangHDNhap);
            tv_ngayHDNhap = itemView.findViewById(R.id.tv_ngayHDNhap);
            tv_soluongHDNhap = itemView.findViewById(R.id.tv_soluongHDNhap);
            tv_dongiaHDNhap = itemView.findViewById(R.id.tv_dongiaHDNhap);
            tv_thanhtienHDNhap = itemView.findViewById(R.id.tv_thanhtienHDNhap);
            img_viewAnhHDNhap = itemView.findViewById(R.id.img_viewAnhHDNhap);
            img_deleteHDN = itemView.findViewById(R.id.img_deleteHDN);
            cv_chitietHDNhap = itemView.findViewById(R.id.cv_chitietHDNhap);

        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
