package com.example.aplicacioncliente.controlador;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplicacioncliente.R;
import com.example.aplicacioncliente.modelos.Linea_Pedido;
import com.example.aplicacioncliente.modelos.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.AdaptadorViewHolder> {
    public List<Producto> listaProductos = new ArrayList<>();
    public Context contexto;
    private List<Linea_Pedido> listaLineasPedido = new ArrayList<>();
    private String pedidoKey;

    public ProductosAdapter(List<Producto> lista , Context contexto, List<Linea_Pedido> listaLineasPedido, String pedidoKey) {
        this.listaProductos = lista;
        this.listaLineasPedido = listaLineasPedido;
        this.contexto = contexto;
        this.pedidoKey = pedidoKey;
    }

    @NonNull
    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto,parent,false);
        AdaptadorViewHolder adaptador =new AdaptadorViewHolder(itemView);
        return adaptador;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorViewHolder holder, int position) {
       Producto producto = listaProductos.get(position);
       try{
           holder.setProducto(producto);
           holder.txtNombreProducto.setText(producto.getNombre());
           holder.txtPrecioProducto.setText(String.valueOf(producto.getPrecio()));

           holder.imgProducto.setImageURI(Uri.parse(producto.getRuta_foto()));
           Glide.with(contexto).load(producto.getRuta_foto()).into(holder.imgProducto);

       }catch (NullPointerException ex){

        }
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class AdaptadorViewHolder extends RecyclerView.ViewHolder{
        private Producto producto;
        private TextView txtNombreProducto, txtPrecioProducto, txtCantidad;
        private ImageView imgProducto;
        private Button btMas, btMenos, btAñadir;

        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCantidad = itemView.findViewById(R.id.txCantidad);
            txtNombreProducto = itemView.findViewById(R.id.txNombreProducto);
            txtPrecioProducto= itemView.findViewById(R.id.txPrecio);
            imgProducto = itemView.findViewById(R.id.fotoProducto);
            btMas = itemView.findViewById(R.id.btMas);
            btMenos = itemView.findViewById(R.id.btMenos);
            btAñadir = itemView.findViewById(R.id.btAñadir);
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

                    if (getProducto().getStock() > Integer.parseInt(txtCantidad.getText().toString())){
                        txtCantidad.setText(String.valueOf(Integer.parseInt(txtCantidad.getText().toString()) + 1));
                    }

                }
            });

            btMenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(txtCantidad.getText().toString()) > 0) {
                        txtCantidad.setText(String.valueOf(Integer.parseInt(txtCantidad.getText().toString()) - 1));
                    }
                }
            });


            btAñadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean modificar = false;

                    if (Integer.parseInt(txtCantidad.getText().toString()) > 0) {
                        for (int i = 0; i < listaLineasPedido.size(); i++) {
                            if (listaLineasPedido.get(i).getIdProducto() == producto.getIdProducto()) {
                                //modifico la linea con ese producto
                                Linea_Pedido linea = listaLineasPedido.get(i);
                                int nuevaCantidad = linea.getCantidadProducto() + Integer.parseInt(txtCantidad.getText().toString());
                                float nuevoSubtotal = (float) nuevaCantidad * (float) (producto.getPrecio() + (producto.getPrecio() * (producto.getIva() / 100f)));
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
                            int cantidad = Integer.parseInt(txtCantidad.getText().toString());
                            float subtotal = (float) cantidad * (float) (producto.getPrecio() + (producto.getPrecio() * (producto.getIva() / 100f)));

                            Linea_Pedido nuevaLinea =
                                    new Linea_Pedido(
                                            String.valueOf(listaLineasPedido.size()),
                                            producto.getIdProducto(),
                                            cantidad,
                                            subtotal
                                    );
                            System.out.println("Añado");
                            listaLineasPedido.add(nuevaLinea);
                        }
                        txtCantidad.setText(String.valueOf(0));
                    }
                }
            });
        }
    }


}
