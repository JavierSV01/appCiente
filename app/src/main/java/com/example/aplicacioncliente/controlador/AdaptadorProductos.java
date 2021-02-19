package com.example.aplicacioncliente.controlador;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplicacioncliente.R;
import com.example.aplicacioncliente.modelos.Linea_Pedido;
import com.example.aplicacioncliente.modelos.Producto;
import com.google.android.gms.common.util.JsonUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorProductos extends RecyclerView.Adapter {

    private ArrayList<Producto> listaProductos;
    private ArrayList<Linea_Pedido> listaLineasPedido;
    Context contexto;
    AdaptadorProductos.AdaptadorComerciosViewHolder acvh;


    public AdaptadorProductos(Context contexto, ArrayList<Producto> listaProductos, ArrayList<Linea_Pedido> listaLineasPedido) {
        this.contexto = contexto;
        this.listaProductos = listaProductos;
        this.listaLineasPedido = listaLineasPedido;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item_comercio = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        acvh = new AdaptadorProductos.AdaptadorComerciosViewHolder(item_comercio, listaLineasPedido);
        return acvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Producto p = listaProductos.get(position);
        acvh.setProducto(p);
        float precio = (float) (p.getPrecio() + (p.getPrecio() * (p.getIva() / 100f)));
        acvh.txPrecio.setText(Float.toString(precio));
        acvh.txNombre.setText(p.getNombre());

        if (p.getRuta_foto() != null && !p.getRuta_foto().equals("")) {
            Glide.with(acvh.fotoPoducto.getContext()).load(Uri.parse(p.getRuta_foto())).into(acvh.fotoPoducto);
        }


/*
        acvh.btAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean modificar = false;

                if (Integer.parseInt(acvh.txCantidad.getText().toString()) > 0) {
                    for (int i = 0; i < listaLineasPedido.size(); i++) {
                        if (listaLineasPedido.get(i).getIdProducto() == acvh.getProducto().getIdProducto()) {
                            //modifico la linea con ese producto
                            Linea_Pedido linea = listaLineasPedido.get(i);
                            int nuevaCantidad = linea.getCantidadProducto() + Integer.parseInt(acvh.txCantidad.getText().toString());
                            float nuevoSubtotal = (float) nuevaCantidad * (float) (acvh.getProducto().getPrecio() + (acvh.getProducto().getPrecio() * (acvh.getProducto().getIva() / 100f)));
                            linea.setSubtotalLinea(nuevoSubtotal);
                            linea.setCantidadProducto(nuevaCantidad);
                            System.out.println("Modifico");
                            listaLineasPedido.add(i, linea);
                            modificar = true;
                        }
                    }
                    if (!modificar) {
                        //Añado la linea con el nuevo producto
                        int cantidad = Integer.parseInt(acvh.txCantidad.getText().toString());
                        float subtotal = (float) cantidad * (float) (acvh.getProducto().getPrecio() + (acvh.getProducto().getPrecio() * (acvh.getProducto().getIva() / 100f)));

                        Linea_Pedido nuevaLinea =
                                new Linea_Pedido(
                                        String.valueOf(listaLineasPedido.size()),
                                        acvh.getProducto().getIdProducto(),
                                        cantidad,
                                        subtotal
                                );
                        System.out.println("Añado");
                        listaLineasPedido.add(nuevaLinea);
                    }
                    acvh.txCantidad.setText(String.valueOf(0));


                }


            }
        });


*/

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class AdaptadorComerciosViewHolder extends RecyclerView.ViewHolder {

        private Producto producto;
        private TextView txNombre, txPrecio, txCantidad;
        private Button btMas, btMenos, btAñadir;
        private CircleImageView fotoPoducto;
        ArrayList<Linea_Pedido> listaLineasPedido;


        public AdaptadorComerciosViewHolder(@NonNull View itemView, ArrayList<Linea_Pedido> listaLineasPedido) {
            super(itemView);
            this.txNombre = itemView.findViewById(R.id.txNombreProducto);
            this.fotoPoducto = itemView.findViewById(R.id.fotoProducto);
            this.txPrecio = itemView.findViewById(R.id.txPrecio);
            this.txCantidad = itemView.findViewById(R.id.txCantidad);
            this.btAñadir = itemView.findViewById(R.id.btAñadir);
            this.btMas = itemView.findViewById(R.id.btMas);
            this.btMenos = itemView.findViewById(R.id.btMenos);
            this.listaLineasPedido = listaLineasPedido;
            botones();
        }

        public Producto getProducto() {
            return producto;
        }

        public void setProducto(Producto producto) {
            this.producto = producto;
        }

        void botones() {
            btMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txCantidad.setText(String.valueOf(Integer.parseInt(txCantidad.getText().toString()) + 1));
                }
            });

            btMenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(txCantidad.getText().toString()) > 0) {
                        txCantidad.setText(String.valueOf(Integer.parseInt(txCantidad.getText().toString()) - 1));
                    }
                }
            });


            btAñadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean modificar = false;

                    if (Integer.parseInt(txCantidad.getText().toString()) > 0) {
                        for (int i = 0; i < listaLineasPedido.size(); i++) {
                            if (listaLineasPedido.get(i).getIdProducto() == getProducto().getIdProducto()) {
                                //modifico la linea con ese producto
                                Linea_Pedido linea = listaLineasPedido.get(i);
                                int nuevaCantidad = linea.getCantidadProducto() + Integer.parseInt(txCantidad.getText().toString());
                                float nuevoSubtotal = (float) nuevaCantidad * (float) (getProducto().getPrecio() + (getProducto().getPrecio() * (getProducto().getIva() / 100f)));
                                //linea.setSubtotalLinea(nuevoSubtotal);
                                //linea.setCantidadProducto(nuevaCantidad);
                                System.out.println("Modifico");
                                listaLineasPedido.get(i).setSubtotalLinea(nuevoSubtotal);
                                listaLineasPedido.get(i).setCantidadProducto(nuevaCantidad);
                                modificar = true;
                            }
                        }
                        if (!modificar) {
                            //Añado la linea con el nuevo producto
                            int cantidad = Integer.parseInt(txCantidad.getText().toString());
                            float subtotal = (float) cantidad * (float) (getProducto().getPrecio() + (getProducto().getPrecio() * (getProducto().getIva() / 100f)));

                            Linea_Pedido nuevaLinea =
                                    new Linea_Pedido(
                                            String.valueOf(listaLineasPedido.size()),
                                            getProducto().getIdProducto(),
                                            cantidad,
                                            subtotal
                                    );
                            System.out.println("Añado");
                            listaLineasPedido.add(nuevaLinea);
                        }
                        txCantidad.setText(String.valueOf(0));
                    }
                }
            });
        }
    }
}
