package com.controlador;

import com.entity.Inmueble;
import com.controlador.util.JsfUtil;
import com.controlador.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.ServletContext;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.bean.ManagedProperty;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "inmuebleController")
@SessionScoped

public class InmuebleController implements Serializable {

    private UploadedFile foto;
    
    @EJB
    private com.controlador.InmuebleFacade ejbFacade;
    private List<Inmueble> items = null;
    private Inmueble selected;

    public InmuebleController() {
       // foto = null;
    }

    public Inmueble getSelected() {
        return selected;
    }

    public void setSelected(Inmueble selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public InmuebleFacade getFacade() {
        return ejbFacade;
    }

    public Inmueble prepareCreate() {
        selected = new Inmueble();
        initializeEmbeddableKey();
        return selected;
    }
    
    public Inmueble buscarInmueble(String pDir){        
        Boolean encontrado=false;
        Inmueble miInmueble=null;
        for(int i=0; i<items.size() && !encontrado; i++){
            if(items.get(i).getDireccion().equals(pDir)){
                encontrado = true;
                miInmueble = items.get(i);
            }
        }        
        return miInmueble;
    }

    public void create() {
        String direccion = selected.getDireccion();
        Inmueble miInmueble = buscarInmueble(direccion);
        
        if(miInmueble==null){
            persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("InmuebleCreated"));
            if (!JsfUtil.isValidationFailed()) {
                items = null;    // Invalidate list of items to trigger re-query.
            }
        }
        else JsfUtil.addErrorMessage("Error: el Inmueble con esta direccion ya se encuentra registrado");
        
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("InmuebleUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("InmuebleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Inmueble> getItems() {
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

    public List<Inmueble> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Inmueble> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Inmueble.class)
    public static class InmuebleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InmuebleController controller = (InmuebleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "inmuebleController");
            return controller.getFacade().find(getKey(value));
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
            if (object instanceof Inmueble) {
                Inmueble o = (Inmueble) object;
                return getStringKey(o.getIdInmueble());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Inmueble.class.getName()});
                return null;
            }
        }

    }
    public void actualizarFoto() throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            if (this.foto.getSize() > 2097152 && selected != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: El archivo no puede ser más de 2mb", ""));
                return;
            }
            
            else if (this.foto.getSize() <= 0 && selected != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: Ud. debe seleccionar un archivo de imagen \".jpg\"", ""));
                return;
            }           

            else if (!this.foto.getFileName().endsWith(".jpg")&& selected != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: El archivo debe ser con extensión \".jpg\"", ""));
                return;
            }
                       
            else if (selected != null) {
                
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String carpetaAvatar = (String) servletContext.getRealPath("/imagenes");

                outputStream = new FileOutputStream(new File(carpetaAvatar + "/" + getFoto().getFileName()));

                inputStream = this.foto.getInputstream();

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                selected.setImg(getFoto().getFileName());
                persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("InmuebleUpdated"));
                selected = null;
                foto=null;
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Avatar actualizado correctamente"));
            }
            else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: debe seleccionar un Inmueble",""));
            }
            
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:" + ex.getMessage(),"Por favor contacte con su administrador " ));
        } finally {
             if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public UploadedFile getFoto() {
        return foto;
    }

    public void setFoto(UploadedFile foto) {
        this.foto = foto;
    }    
}
