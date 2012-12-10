package org.openprovenance.prov.xml;

import java.util.List;
import java.util.LinkedList;
import javax.xml.namespace.QName;


public class BeanTraversal {
    final private BeanConstructor c;
    final private ProvFactory pFactory;
    ProvUtilities u=new ProvUtilities();

    public BeanTraversal(BeanConstructor c, ProvFactory pFactory) {
	this.c=c;
	this.pFactory=pFactory;
    }

    public Document convert(Document b) {

	List<NamedBundle> bRecords = new LinkedList<NamedBundle>();

	List<Statement> sRecords = new LinkedList<Statement>();

	for (Statement s : u.getStatement(b)) {
	    if (s instanceof Entity) {
		sRecords.add(convert((Entity)s));
	    } else if (s instanceof Activity) {
		sRecords.add(convert((Activity)s));
	    } else if (s instanceof Agent) {
		sRecords.add(convert((Agent)s));
	    } else {
		sRecords.add(convertRelation((Relation0)s));
	    }

	}

	for (NamedBundle bu : u.getNamedBundle(b)) {
	    NamedBundle o = convert(bu);
	    if (o != null)
		bRecords.add(o);

	}
	return c.newDocument(b.getNss(), sRecords, bRecords);
    }

    public NamedBundle convert(NamedBundle b) {
	List<Statement> sRecords = new LinkedList<Statement>();

	for (Statement s : u.getStatement(b)) {
	    if (s instanceof Entity) {
		sRecords.add(convert((Entity)s));
	    } else if (s instanceof Activity) {
		sRecords.add(convert((Activity)s));
	    } else if (s instanceof Agent) {
		sRecords.add(convert((Agent)s));
	    } else {
		sRecords.add(convertRelation((Relation0)s));
	    }

	}
	return c.newNamedBundle(b.getId(), b.getNss(), sRecords);
    }

   
    public List<Attribute> convertTypeAttributes(HasType e, List<Attribute> acc) {
	List<Object> types=e.getType();
	for (Object type : types) {
	    acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, type));
	}
	return acc;
    }

    public List<Attribute> convertLabelAttributes(HasLabel e, List<Attribute> acc) {
   	List<InternationalizedString> labels = e.getLabel();
   	for (InternationalizedString label : labels) {
   	    acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_LABEL,label));
   	}
   	return acc;
    }
    
    public List<Attribute> convertRoleAttributes(HasRole e, List<Attribute> acc) {
   	List<Object> roles = e.getRole();
   	for (Object role : roles) {
   	    acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_ROLE,role));
   	}
   	return acc;
    }
    
    public List<Attribute> convertLocationAttributes(HasLocation e, List<Attribute> acc) {
        List<Object> locations = e.getLocation();
        for (Object location : locations) {
            acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_LOCATION,location));
        }
        return acc;
    }

    
    public Object convertValueAttributes(HasValue e, List<Attribute> acc) {
        Object value = e.getValue();
        if (value==null) return acc;
        acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_VALUE,value));
        return acc;     
    }

    public List<Attribute> convertAttributes(HasExtensibility e, List<Attribute> acc) {
	acc.addAll(e.getAny());
	return acc;
    }


       

    public Entity convert(Entity e) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(e,attrs);
	convertLabelAttributes(e,attrs);
	convertLocationAttributes(e,attrs);	
	convertValueAttributes(e,attrs);
	convertAttributes(e,attrs);
	return c.newEntity(e.getId(), attrs);
    }

    public Activity convert(Activity e) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(e,attrs);
	convertLabelAttributes(e,attrs);
	convertLocationAttributes(e,attrs);	
	convertAttributes(e,attrs);
	return c.newActivity(e.getId(), e.getStartTime(), e.getEndTime(), attrs);
    }
    

    public Agent convert(Agent e) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(e,attrs);
	convertLabelAttributes(e,attrs);
	convertLocationAttributes(e,attrs);	
	convertAttributes(e,attrs);
	return c.newAgent(e.getId(), attrs);
    }

    public Relation0 convertRelation(Relation0 o) {
	// no visitors, so ...
	if (o instanceof Used) {
	    return convert((Used) o);
	} else if (o instanceof WasInformedBy) {
	    return convert((WasInformedBy) o);
	} else if (o instanceof WasInfluencedBy) {
	    return convert((WasInfluencedBy) o);
	} else if (o instanceof WasGeneratedBy) {
	    return convert((WasGeneratedBy) o);
	} else if (o instanceof WasInvalidatedBy) {
	    return convert((WasInvalidatedBy) o);
	} else if (o instanceof WasStartedBy) {
	    return convert((WasStartedBy) o);
	} else if (o instanceof WasEndedBy) {
	    return convert((WasEndedBy) o);

	} else if (o instanceof WasDerivedFrom) {
	    return convert((WasDerivedFrom) o);

	} else if (o instanceof WasAttributedTo) {
	    return convert((WasAttributedTo) o);
	} else if (o instanceof WasAssociatedWith) {
	    return convert((WasAssociatedWith) o);
	} else if (o instanceof ActedOnBehalfOf) {
	    return convert((ActedOnBehalfOf) o);

	} else if (o instanceof AlternateOf) {
	    return convert((AlternateOf) o);
	} else if (o instanceof SpecializationOf) {
	    return convert((SpecializationOf) o);
	} else if (o instanceof MentionOf) {
	    return convert((MentionOf) o);
	} else if (o instanceof HadMember) {
	    return convert((HadMember) o);

	} else {
	    throw new UnsupportedOperationException("Unknown relation type "
		    + o);
	}
    }

    final private QName q(Ref id) {
	return (id==null) ? null: id.getRef();
    }

    public Used convert(Used use) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(use,attrs);
	convertLabelAttributes(use,attrs);
	convertLocationAttributes(use,attrs);	
 	convertRoleAttributes(use,attrs);
	convertAttributes(use,attrs);
	return c.newUsed(use.getId(), q(use.getActivity()), q(use.getEntity()), use.getTime(), attrs);
    }
    
    public WasGeneratedBy convert(WasGeneratedBy gen) {
 	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(gen,attrs);
 	convertLabelAttributes(gen,attrs);
 	convertLocationAttributes(gen,attrs);	
 	convertRoleAttributes(gen,attrs);
 	convertAttributes(gen,attrs);
 	return c.newWasGeneratedBy(gen.getId(), q(gen.getEntity()), q(gen.getActivity()), gen.getTime(), attrs);
     }

    
    public WasInvalidatedBy convert(WasInvalidatedBy inv) {
 	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(inv,attrs);
 	convertLabelAttributes(inv,attrs);
 	convertLocationAttributes(inv,attrs);	
 	convertRoleAttributes(inv,attrs);
 	convertAttributes(inv,attrs);
 	return c.newWasInvalidatedBy(inv.getId(), q(inv.getEntity()), q(inv.getActivity()), inv.getTime(), attrs);
     }

    public WasStartedBy convert(WasStartedBy start) {
 	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(start,attrs);
 	convertLabelAttributes(start,attrs);
 	convertLocationAttributes(start,attrs);	
 	convertRoleAttributes(start,attrs);
 	convertAttributes(start,attrs);
 	return c.newWasStartedBy(start.getId(), q(start.getActivity()), q(start.getTrigger()), q(start.getStarter()), start.getTime(), attrs);
     }

    public WasEndedBy convert(WasEndedBy end) {
 	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(end,attrs);
 	convertLabelAttributes(end,attrs);
 	convertLocationAttributes(end,attrs);	
 	convertRoleAttributes(end,attrs);
 	convertAttributes(end,attrs);
 	return c.newWasEndedBy(end.getId(), q(end.getActivity()), q(end.getTrigger()), q(end.getEnder()), end.getTime(), attrs);
     }


   

   
    public WasInformedBy convert(WasInformedBy o) {
	throw new UnsupportedOperationException();
    }

    public WasInfluencedBy convert(WasInfluencedBy o) {
	throw new UnsupportedOperationException();
    }

    public WasDerivedFrom convert(WasDerivedFrom deriv) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(deriv,attrs);
 	convertLabelAttributes(deriv,attrs);
 	convertAttributes(deriv,attrs);
 	return c.newWasDerivedFrom(deriv.getId(), 
 	                           q(deriv.getGeneratedEntity()), 
 	                           q(deriv.getUsedEntity()), 
 	                           q(deriv.getActivity()), 
 	                           q(deriv.getGeneration()), 
 	                           q(deriv.getUsage()), 
 	                           attrs);
    }

    public WasAssociatedWith convert(WasAssociatedWith o) {
	throw new UnsupportedOperationException();
    }

    public WasAttributedTo convert(WasAttributedTo o) {
	throw new UnsupportedOperationException();
    }

    public ActedOnBehalfOf convert(ActedOnBehalfOf o) {
	throw new UnsupportedOperationException();
    }

    public AlternateOf convert(AlternateOf o) {
	throw new UnsupportedOperationException();
    }

    public SpecializationOf convert(SpecializationOf o) {
	throw new UnsupportedOperationException();
    }

    public MentionOf convert(MentionOf o) {
	throw new UnsupportedOperationException();
    }
    //TODO: only supporting one member in the relation
    // note: lots of test to support scruffy provenance
    public HadMember convert(HadMember o) {
	throw new UnsupportedOperationException();
/*remember, scruffy hadmember in provn, has a null in get(0)!!
	return deprecated.convertHadMember(deprecated.convert((o.getCollection()==null) ? null: 	                                    
	                                      o.getCollection().getRef()),
	                          ((o.getEntity()==null || (o.getEntity().isEmpty())) ?
	                        	  deprecated.convert(null) :
	                        	  deprecated.convert((o.getEntity().get(0)==null) ? 
	                        		     null :
	                        	             o.getEntity().get(0).getRef()))); */
    }
    
	
}
