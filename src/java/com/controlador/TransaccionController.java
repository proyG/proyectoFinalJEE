package com.controlador;

import com.entity.Transaccion;
import com.controlador.util.JsfUtil;
import com.controlador.util.JsfUtil.PersistAction;
import com.entity.Inmueble;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "transaccionController")
@SessionScoped
public class TransaccionController implements Serializable {
       
    @ManagedProperty("#{inmuebleController}")
    private InmuebleController inmuebleControlador;
    private List<Inmueble> itemsInmueble = null;
    private Inmueble selectedInmueble;

    @EJB
    private com.controlador.TransaccionFacade ejbFacade;
    private List<Transaccion> items = null;
    private Transaccion selected;

    public TransaccionController() {
    }

    public Inmueble getSelectedInmueble() {
        return selectedInmueble;
    }

    public void setSelectedInmueble(Inmueble selectedInmueble) {
        this.selectedInmueble = selectedInmueble;
    }

    public Transaccion getSelected() {
        return selected;
    }

    public void setSelected(Transaccion selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TransaccionFacade getFacade() {
        return ejbFacade;
    }

    public Transaccion prepareCreate() {
        selected = new Transaccion();
        initializeEmbeddableKey();
        return selected;
    }
    
    public void create() {
        try {

            if (selectedInmueble != null) {
                                
                selected.setInmueble(selectedInmueble);
                persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TransaccionCreated"));                                

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: debe seleccionar un Inmueble", ""));
            }

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:" + ex.getMessage(), "Por favor contacte con su administrador "));
        } finally {
            if (selectedInmueble != null) {
                selectedInmueble = null;
            }
        }
        
         if (!JsfUtil.isValidationFailed()) {
         items = null;    // Invalidate list of items to trigger re-query.
         }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TransaccionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TransaccionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    //items de las transacciones 
    public List<Transaccion> getItems() {
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

    public List<Transaccion> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Transaccion> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Transaccion.class)
    public static class TransaccionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TransaccionController controller = (TransaccionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "transaccionController");
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
            if (object instanceof Transaccion) {
                Transaccion o = (Transaccion) object;
                return getStringKey(o.getIdTransaccion());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Transaccion.class.getName()});
                return null;
            }
        }

    }

    //GET creado para acceder a las inmuebles de la managed bean
    public InmuebleController getInmuebleControlador() {
        return inmuebleControlador;
    }
    //SET creado para acceder a las inmuebles de la managed bean
    public void setInmuebleControlador(InmuebleController inmuebleControlador) {
        this.inmuebleControlador = inmuebleControlador;
    }

    public List<Inmueble> getItemsInmuebles() {        
        itemsInmueble = inmuebleControlador.getItems();        
        return itemsInmueble;
    }    
    
    public List<Inmueble> getItemsAvailableSelectManyInmuebles() {
        return inmuebleControlador.getFacade().findAll();
    }

    public List<Inmueble> getItemsAvailableSelectOneInmuebles() {
        return inmuebleControlador.getFacade().findAll();
    }
}
