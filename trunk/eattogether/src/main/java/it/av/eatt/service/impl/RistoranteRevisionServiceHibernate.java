package it.av.eatt.service.impl;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.RistoranteRevision;
import it.av.eatt.service.RistoranteRevisionService;

public class RistoranteRevisionServiceHibernate 
    extends ApplicationServiceHibernate<RistoranteRevision> implements RistoranteRevisionService{

    @Override
    public RistoranteRevision insert(RistoranteRevision revision) throws JackWicketException {
        return save(revision);
    }
}
