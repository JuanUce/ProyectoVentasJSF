package Control;

import Modelo.FotosProductos;
import Control.util.JsfUtil;
import Control.util.JsfUtil.PersistAction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.UploadedFile;

@Named("fotosProductosController")
@SessionScoped
public class FotosProductosController implements Serializable {

    @EJB
    private Control.FotosProductosFacade ejbFacade;
    private List<FotosProductos> items = null;
    private FotosProductos selected;
    private UploadedFile archivo;
    private String aux;
    private String rutaURL;
    
    public FotosProductosFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(FotosProductosFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public UploadedFile getArchivo() {
        return archivo;
    }

    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }

    public String getAux() {
        return aux;
    }

    public void setAux(String aux) {
        this.aux = aux;
    }

    public String getRutaURL() {
        return rutaURL;
    }

    public void setRutaURL(String rutaURL) {
        this.rutaURL = rutaURL;
    }

    
    
    public void nuevodocumento() {

        System.out.println("MIME_TYPE:" + getArchivo().getContentType());
        System.out.println("TAMAÑO:" + getArchivo().getSize());

        System.out.println("ENTENSIÓN PNG:" + getArchivo().getFileName().endsWith(".png"));
        System.out.println("ENTENSIÓN JPG:" + getArchivo().getFileName().endsWith(".jpg"));
        System.out.println("ENTENSIÓN GIF:" + getArchivo().getFileName().endsWith(".gif"));
//       if (getArchivo().getFileName().endsWith(".png") ||getArchivo().getFileName().endsWith(".jpg")||
//               getArchivo().getFileName().endsWith(".gif"))   
//       {
//           if (SubirArchivo()) {
//               create();
//               aux="null";
//           }
//       }else{
//           FacesMessage mensaje = new  FacesMessage("El archivo no es una imagen");
//           FacesContext.getCurrentInstance().addMessage(null, mensaje);
//           selected=null;
//       }
//   

        if (SubirArchivo()) {
            System.out.println("INGRESA CREATE1");
            create1();
            aux="";
            System.out.println("INGRESA CREATE2");
        }
    }



public Boolean SubirArchivo(){
        
       try {
           aux="resources\\docsProductos";
          
           System.out.println("RUTA = " +aux); 
         System.out.println("RUTA DE SISTEMA "+JsfUtil.getPath());
           File arch = new File(JsfUtil.getPath()+ aux);     
           if (!arch.exists()) {
               arch.mkdirs();
               
           }
           
           copiar_archivo(getArchivo().getFileName(),getArchivo().getInputstream());
            return true;
       } catch (Exception e) {
            return false;
       }
     
   
   }
    
   
   public void copiar_archivo(String nombre_archivo,InputStream  in){
       try{
       //EXPLICACION DE LAS RUTAS 
       //En windows las rutas vienen separado EJEMPLO:   D:\PRACTICAS_JSF\Ventas\build\web\resources\docsProductos
       //si deseamos: añadir este caracter "\" como separadpr Java no lo reconoce para ello se utiliza "\\"
       //Para abrir una ruta en el navegador se utiliza como caracter "/"
       //por ello se utiliza rutaURL
       
       
       rutaURL="/docsProductos/"+nombre_archivo; 
       
       
       System.out.println("RUTA BASE A GUARDAR"+rutaURL);
       aux=aux+"\\"+nombre_archivo;
   
    //  String reemplazo=aux.replaceAll("/", "u005C");
     // System.out.println("REEMLAZO ES"+reemplazo);
        System.out.println("RUTA OK  "+aux);
        System.out.println("Ruta Real  "+JsfUtil.getPath()+ aux);
        OutputStream out= new FileOutputStream(new File(JsfUtil.getPath()+ aux));
       int read=0;
       byte[] bytes=new byte[1024];
       while ((read=in.read(bytes))!= -1){
         out.write(bytes,0,read);
           
       }
       
        System.out.println("Se va a guadar");
        aux=aux.substring(10);
         System.out.println("Ruta en la base  " +aux);
         in.close();
         out.flush();
         out.close();
       }catch(Exception e){
      JsfUtil.addErrorMessage(e, "ERROR AL COPAI ARCHIVO");
       }
              
       
   
    }
       
       
    
    public FotosProductosController() {
    }

    public FotosProductos getSelected() {
        return selected;
    }

    public void setSelected(FotosProductos selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FotosProductosFacade getFacade() {
        return ejbFacade;
    }

    public FotosProductos prepareCreate() {
        selected = new FotosProductos();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setStatus(1);
        
        selected.setRuta(aux);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FotosProductosCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    
    public void create1() {
        selected.setStatus(1);
        
//        StringBuilder cadena= new StringBuilder(aux);
//        cadena.setCharAt(14, '/');
//        aux=cadena.toString();
//        String rutaGuardar="/"+aux;
//         System.out.println("/LO QUE SE VA A GUARDAR"+rutaGuardar);
       
        selected.setRuta(rutaURL);
        ejbFacade.create(selected);
        
        
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FotosProductosUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FotosProductosDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<FotosProductos> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public FotosProductos getArchivosProductos(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<FotosProductos> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<FotosProductos> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    

}

    @FacesConverter(forClass = FotosProductos.class)
public static class FotosProductosControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        FotosProductosController controller = (FotosProductosController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "fotosProductosController");
        return controller.getArchivosProductos(getKey(value));
    }

    java.lang.Integer getKey(String value) {
        java.lang.Integer key;
        key = Integer.valueOf(value);
        return key;
    }

    String getStringKey(java.lang.Integer value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof FotosProductos) {
            FotosProductos o = (FotosProductos) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), FotosProductos.class.getName()});
            return null;
        }
    }

}

}
