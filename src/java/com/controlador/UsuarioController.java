package com.controlador;

import com.Encriptacion.Encrypt;
import com.entity.Usuario;
import com.controlador.util.JsfUtil;
import com.controlador.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;


@ManagedBean(name = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    @EJB
    private com.controlador.UsuarioFacade ejbFacade;
    private List<Usuario> items = null;
    private Usuario selected;
    
    private int id_usuario;
    private String usuario;
    private String contrasenia;
    public HttpSession miSession;
    
    static int contador;
    static int cont=0;
    //inicializo el contador en cero
    static Date fechaActual = new Date();
    static Calendar calendarActual = Calendar.getInstance();	
    static Boolean banCont = false;

    public UsuarioController() {
        setContador(0);
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }
    
    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public String login() {
        String pag = "index";
        cont++;

        if (cont < 3) {

            List<Usuario> usuarios = getFacade().findAll();
            contrasenia = Encrypt.sha512(contrasenia);

            boolean ban = busquedaUsuario(2);

            if (ban == true) {
                cont = 0;
                banCont = false;
                JsfUtil.addSuccessMessage("Bienvienido: " + usuario);
                pag = "principal";
            } else {
                JsfUtil.addErrorMessage("Nombre de usuario o Contraseña incorrectos");
                pag = "index";
            }
        } else {
            if (banCont == false) {
                fechaActual = new Date();
                calendarActual.setTime(fechaActual); // Configuramos la fecha que se recibe	
                calendarActual.add(Calendar.SECOND, 30);  // numero de horas a añadir, o restar en caso de horas<0                    
                JsfUtil.addErrorMessage("Numero máximo de intentos permitidos, vuelva a intentarlo despues de 30 segundos");
                banCont = true;
            } else {
                Date fecha = new Date();
                Calendar calend = Calendar.getInstance();
                calend.setTime(fecha);

                if (calend.after(calendarActual)) {
                    cont = 0;
                    banCont = false;
                    JsfUtil.addErrorMessage("Sesion Reactivada, Inicie Sesion nuevamente");
                } else {
                    JsfUtil.addErrorMessage("Numero máximo de intentos permitidos, vuelva a intentarlo despues de 30 segundos");
                }
            }
        }
        selected = null;
        usuario = null;
        return pag;
    }
    
    public boolean busquedaUsuario(int opcion){
        
        //verifico si el usuario existe o no
        List<Usuario> usuarios = getFacade().findAll();
        boolean banExiste = false;
        
        if (opcion == 1) {
            for (int i = 0; i < usuarios.size() && !banExiste; i++) {
                if (usuario.equals(usuarios.get(i).getUsername())) {
                    banExiste = true;
                }
            }
        }
        else if(opcion == 2){
            for (int i = 0; i < usuarios.size() && !banExiste; i++) {
                if (usuario.equals(usuarios.get(i).getUsername()) && contrasenia.equals(usuarios.get(i).getPassword())) {
                    miSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                    miSession.setAttribute("usuario", usuario);
                    banExiste = true;
                }
            }            
        }
        else {
            for (int i = 0; i < usuarios.size() && !banExiste; i++) {
                if (contrasenia.equals(usuarios.get(i).getPassword()) && selected.getUsername().equals(usuarios.get(i).getUsername())) {
                    banExiste = true;
                }

            }
        }
        return banExiste;
    }
    
    public String getUsuario() {
        return usuario;
    }
 
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
 
    public String getContrasenia() {
        return contrasenia;
    }
 
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }  

    public Usuario getSelected() {
        return selected;
    }

    public void setSelected(Usuario selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsuarioFacade getFacade() {
        return ejbFacade;
    }

    public Usuario prepareCreate() {
        selected = new Usuario();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setPassword(Encrypt.sha512(selected.getPassword()));
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    //registrar un usuario desde formulario de registro
    public void RegistrarUsuario(){
        
        //existe == true ; el usuario ya se encuentra registrado
        boolean existe = busquedaUsuario(1);
        
        if (existe == false) {
            //validar contraseña alfanumerica, minusculas, mayusculas:
            boolean minus = false, mayus = false, num = false, especial = false;
            for (int i = 0; i < contrasenia.length(); i++) {
                char c = contrasenia.charAt(i);
                if (c >= 'a' && c <= 'z') {
                    minus = true;
                } else if (c >= 'A' && c <= 'Z') {
                    mayus = true;
                } else if (c >= '0' && c <= '9') {
                    num = true;
                } else if ((int) c > 32 && (int) c <= 47) {
                    especial = true;
                } else if ((int) c >= 58 && (int) c <= 64) {
                    especial = true;
                } else if ((int) c >= 91 && (int) c <= 96) {
                    especial = true;
                } else if ((int) c >= 123 && (int) c <= 126) {
                    especial = true;
                } else if ((int) c == 168 || (int) c == 173) {
                    especial = true;
                }
            }
            if (minus == true && mayus == true && num == true && especial == true) {

                selected = new Usuario();
                selected.setUsername(usuario);
                selected.setPassword(Encrypt.sha512(contrasenia));

                persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated"));
                if (!JsfUtil.isValidationFailed()) {
                    items = null;    // Invalidate list of items to trigger re-query.
                }
            } else {
                JsfUtil.addErrorMessage("La contraseña debe tener letras mayusculas, minusculas, numeros y caracteres especiales");
            }
        }
        else {
            JsfUtil.addErrorMessage("El usuario ya existe, por favor ingresa otro nombre");
        }
        
        selected=null;
        usuario = null;        
    }       

    public void update() {                
        
        contrasenia = Encrypt.sha512(contrasenia);
        
        List<Usuario> miLista = getFacade().findAll();
        
        Boolean encontrado = busquedaUsuario(3);
        
        if (encontrado == true) {
            contrasenia = selected.getPassword();
           boolean minus = false, mayus = false, num = false, especial = false;
            for (int i = 0; i < contrasenia.length(); i++) {
                char c = contrasenia.charAt(i);
                if (c >= 'a' && c <= 'z') {
                    minus = true;
                } else if (c >= 'A' && c <= 'Z') {
                    mayus = true;
                } else if (c >= '0' && c <= '9') {
                    num = true;
                } else if ((int) c > 32 && (int) c <= 47) {
                    especial = true;
                } else if ((int) c >= 58 && (int) c <= 64) {
                    especial = true;
                } else if ((int) c >= 91 && (int) c <= 96) {
                    especial = true;
                } else if ((int) c >= 123 && (int) c <= 126) {
                    especial = true;
                } else if ((int) c == 168 || (int) c == 173) {
                    especial = true;
                }
            }

            if (minus == true && mayus == true && num == true && especial == true) {
                
                selected.setPassword(Encrypt.sha512(contrasenia));

                persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
                if (!JsfUtil.isValidationFailed()) {
                    items = null;    // Invalidate list of items to trigger re-query.
                }
            } else {
                JsfUtil.addErrorMessage("la contraseña debe tener letras mayusculas, minusculas y numeros");
            }
        } else {
            JsfUtil.addErrorMessage("Contraseña actual Incorrecta");
        }
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UsuarioDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Usuario> getItems() {
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

    public List<Usuario> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Usuario> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Usuario.class)
    public static class UsuarioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioController controller = (UsuarioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuarioController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Usuario) {
                Usuario o = (Usuario) object;
                return getStringKey(o.getUsername());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Usuario.class.getName()});
                return null;
            }
        }
    }
}
