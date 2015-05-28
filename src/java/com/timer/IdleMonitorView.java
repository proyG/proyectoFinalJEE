/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timer;

import com.controlador.UsuarioController;
import com.controlador.util.JsfUtil;
//import com.entity.util.JsfUtil.PersistAction;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
 
@ManagedBean
public class IdleMonitorView {
    public void onIdle() throws IOException {     
        /*FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                        "Sin Actividad", "Su session a cadudacdo por inactividad"));*/
        
        JsfUtil.addSuccessMessage("Sin Actividad, Su session a cadudacdo por inactividad ");
        FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/proyectoFinalJEE/faces/index.xhtml");
    }
 
    public void onActive() {
        
        JsfUtil.addSuccessMessage("Bienvenido nuevamente, sesion activa en estos momentos");
        /*FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "Bienvenido nuevamente", "Session activa en estos momentos"));*/
    }
}
