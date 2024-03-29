package it.av.eatt.web.page;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.RistoranteRevision;
import it.av.eatt.web.util.RistoranteRevisionUtil;
import it.av.eatt.web.util.TextDiffRender;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * The panel displays the revisions form
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class RistoranteRevisionsPanel extends Panel {
    private static final long serialVersionUID = 1L;
    private PropertyListView<RistoranteRevision> productsVersionsList;
    private List<RistoranteRevision> revisions = new ArrayList<RistoranteRevision>();
    final FeedbackPanel feedbackPanel;

    /**
     * Constructor
     * 
     * @param revisions
     * @param id
     * @param feedbackPanel
     */
    public RistoranteRevisionsPanel(String id, final FeedbackPanel feedbackPanel) {
        super(id);
        this.feedbackPanel = feedbackPanel;
        productsVersionsList = new PropertyListView<RistoranteRevision>("versions", revisions) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<RistoranteRevision> item) {
                item.add(new Label("ristoranteRevision.revisionNumber"));
                item.add(new Label("ristoranteRevision.modificationTime"));
                item.add(new Label("ristoranteRevision.name").setEscapeModelStrings(false));
                item.add(new Label("ristoranteRevision.address").setEscapeModelStrings(false));
                item.add(new Label("ristoranteRevision.description").setEscapeModelStrings(false));
            }
        };
        add(productsVersionsList.setOutputMarkupId(true));

    }

    /**
     * Fill with fresh data the repeater
     * 
     * @throws JackWicketException
     */
    public void refreshRevisionsList(Ristorante ristoSelected) {
        try {
            if (ristoSelected != null) {
                revisions = RistoranteRevisionUtil.cloneList(ristoSelected.getRevisions());
                if (revisions.size() > 1) {
                    // Latest two releases
                    Ristorante r1 = revisions.get(revisions.size() - 1).getRistoranteRevision();
                    Ristorante r2 = revisions.get(revisions.size() - 2).getRistoranteRevision();
                    performDiff(r2, r1);
                }

                productsVersionsList.setModelObject(revisions);
            } else {
                productsVersionsList.setModelObject(revisions);
            }
        } catch (JackWicketException e) {
            feedbackPanel.error(e.getMessage());
        }
    }

    public final PropertyListView<RistoranteRevision> getProductsVersionsList() {
        return productsVersionsList;
    }

    public final void setProductsVersionsList(PropertyListView<RistoranteRevision> productsVersionsList) {
        this.productsVersionsList = productsVersionsList;
    }

    private void performDiff(Ristorante ori, Ristorante newVer) throws JackWicketException {
        TextDiffRender diffRender = new TextDiffRender();

        String[] diff = diffRender.render(ori.getDescription(), newVer.getDescription());
        ori.setDescription(diff[0]);
        newVer.setDescription(diff[1]);

        diff = diffRender.render(ori.getName(), newVer.getName());
        ori.setName(diff[0]);
        newVer.setName(diff[1]);

        diff = diffRender.render(ori.getAddress(), newVer.getAddress());
        ori.setAddress(diff[0]);
        newVer.setAddress(diff[1]);
    }
}
