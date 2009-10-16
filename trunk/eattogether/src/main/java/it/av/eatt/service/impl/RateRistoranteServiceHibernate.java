package it.av.eatt.service.impl;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.RateOnRistorante;
import it.av.eatt.service.RateRistoranteService;

public class RateRistoranteServiceHibernate 
    extends ApplicationServiceHibernate<RateOnRistorante> implements RateRistoranteService{

    @Override
    public RateOnRistorante insert(RateOnRistorante rate) throws JackWicketException {
        return save(rate);
    }
}
