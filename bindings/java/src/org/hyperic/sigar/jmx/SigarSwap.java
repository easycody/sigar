/*
 * Copyright (C) [2004, 2005, 2006, 2007], Hyperic, Inc.
 * This file is part of SIGAR.
 * 
 * SIGAR is free software; you can redistribute it and/or modify
 * it under the terms version 2 of the GNU General Public License as
 * published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

package org.hyperic.sigar.jmx;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * Sigar JMX MBean implementation for the <code>Swap</code> information
 * package. Provides an OpenMBean conform implementation.
 * 
 * @author Bjoern Martin
 * @since 1.5
 */
public class SigarSwap extends AbstractMBean {

    private static final String MBEAN_TYPE = "Swap";

    private static final MBeanInfo MBEAN_INFO;

    private static final MBeanAttributeInfo MBEAN_ATTR_FREE;

    private static final MBeanAttributeInfo MBEAN_ATTR_TOTAL;

    private static final MBeanAttributeInfo MBEAN_ATTR_USED;

    private static final MBeanConstructorInfo MBEAN_CONSTR_SIGAR;

    private static MBeanParameterInfo MBEAN_PARAM_SIGAR;

    static {
        MBEAN_ATTR_FREE = new MBeanAttributeInfo("Free", "long",
                "The amount of free swap memory, in [bytes]", true, false,
                false);
        MBEAN_ATTR_TOTAL = new MBeanAttributeInfo("Total", "long",
                "The total amount of swap memory, in [bytes]", true, false,
                false);
        MBEAN_ATTR_USED = new MBeanAttributeInfo("Used", "long",
                "The amount of swap memory in use, in [bytes]", true, false,
                false);
        MBEAN_PARAM_SIGAR = new MBeanParameterInfo("sigar", Sigar.class
                .getName(), "The Sigar instance to use to fetch data from");
        MBEAN_CONSTR_SIGAR = new MBeanConstructorInfo(
                SigarSwap.class.getName(),
                "Creates a new instance, using the Sigar instance "
                        + "specified to fetch the data.",
                new MBeanParameterInfo[] { MBEAN_PARAM_SIGAR });
        MBEAN_INFO = new MBeanInfo(
                SigarSwap.class.getName(),
                "Sigar Swap MBean, provides raw data for the swap memory "
                        + "configured on the system. Uses an internal cache that "
                        + "invalidates within 5 seconds.",
                new MBeanAttributeInfo[] { MBEAN_ATTR_FREE, MBEAN_ATTR_TOTAL,
                        MBEAN_ATTR_USED },
                new MBeanConstructorInfo[] { MBEAN_CONSTR_SIGAR }, null, null);

    }

    /**
     * Object name this instance will give itself when being registered to an
     * MBeanServer.
     */
    private final String objectName;

    /**
     * Creates a new instance, using the Sigar instance specified to fetch the
     * data.
     * 
     * @param sigar
     *            The Sigar instance to use to fetch data from
     * 
     * @throws IllegalArgumentException
     *             If an unexpected Sigar error occurs
     */
    public SigarSwap(Sigar sigar) throws IllegalArgumentException {
        super(sigar, CACHED_5SEC);

        this.objectName = SigarInvokerJMX.DOMAIN_NAME + ":" + MBEAN_ATTR_TYPE
                + "=Swap";
    }

    /**
     * Object name this instance will give itself when being registered to an
     * MBeanServer.
     */
    public String getObjectName() {
        return this.objectName;
    }

    /**
     * @return The amount of free swap memory, in [bytes]
     */
    public long getFree() {
        try {
            return sigar.getSwap().getFree();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    /**
     * @return The total amount of swap memory, in [bytes]
     */
    public long getTotal() {
        try {
            return sigar.getSwap().getTotal();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    /**
     * @return The amount of swap memory in use, in [bytes]
     */
    public long getUsed() {
        try {
            return sigar.getSwap().getUsed();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    // -------
    // Implementation of the DynamicMBean interface
    // -------

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.DynamicMBean#getAttribute(java.lang.String)
     */
    public Object getAttribute(String attr) throws AttributeNotFoundException,
            MBeanException, ReflectionException {
        if (MBEAN_ATTR_FREE.getName().equals(attr)) {
            return new Long(getFree());

        } else if (MBEAN_ATTR_TOTAL.getName().equals(attr)) {
            return new Long(getTotal());

        } else if (MBEAN_ATTR_USED.getName().equals(attr)) {
            return new Long(getUsed());

        } else {
            throw new AttributeNotFoundException(attr);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.management.DynamicMBean#setAttribute(javax.management.Attribute)
     */
    public void setAttribute(Attribute attr) throws AttributeNotFoundException {
        throw new AttributeNotFoundException(attr.getName());
    }

    /*
     * (non-Javadoc)
     * @see javax.management.DynamicMBean#invoke(java.lang.String,
     *      java.lang.Object[], java.lang.String[])
     */
    public Object invoke(String actionName, Object[] params, String[] signature)
            throws ReflectionException {
        throw new ReflectionException(new NoSuchMethodException(actionName),
                actionName);
    }

    /*
     * (non-Javadoc)
     * @see javax.management.DynamicMBean#getMBeanInfo()
     */
    public MBeanInfo getMBeanInfo() {
        return MBEAN_INFO;
    }
}