/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Modelo.TiposPago;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Juan Quishpe
 */
@Stateless
public class TiposPagoFacade extends AbstractFacade<TiposPago> {

    @PersistenceContext(unitName = "VentasPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TiposPagoFacade() {
        super(TiposPago.class);
    }
    
}
