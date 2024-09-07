/*
 *   Copyright (c) 2016.  Jefferson Lab (JLab). All rights reserved. Permission
 *   to use, copy, modify, and distribute  this software and its documentation for
 *   educational, research, and not-for-profit purposes, without fee and without a
 *   signed licensing agreement.
 *
 *   IN NO EVENT SHALL JLAB BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, SPECIAL
 *   INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING
 *   OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF JLAB HAS
 *   BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *   JLAB SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 *   THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *   PURPOSE. THE CLARA SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
 *   PROVIDED HEREUNDER IS PROVIDED "AS IS". JLAB HAS NO OBLIGATION TO PROVIDE
 *   MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *   This software was developed under the United States Government license.
 *   For more information contact author at gurjyan@jlab.org
 *   Department of Experimental Nuclear Physics, Jefferson Lab.
 */

package org.jlab.epsci.dpux.core;

public enum ACodaType {
    USR  (10),
    ACTOR  (11),
    HISTOGRAMSINK(12),
    DEVNULLSINK(13),
    LOADBALANCER(20),
    REASSEMBLE  (21),
    PACKETIZER (22),
    APPLICATION  (30),
    SHELLPROCESS(31),
    DOCKERCONTAINER(32),
    ET  (40),
    ETSOURCE(41),
    FILESOURCE(42),
    SLC  (110),
    WNC  (210),
    ER   (310),
    FCS  (410),
    PEB  (510),
    PAGG (560),
    SEB  (610),
    SAGG (660),
    EB   (710),
    EBER (810),
    DC   (910),
    FPGA (960),
    ROC  (1010),
    GT   (1110),
    TS   (1210),
    VTP (1222),
    SMS  (1310),
    RCS  (1410),
    SINK(1510);

    private int p;

    ACodaType(int s){
        p = s;
    }

    public int priority(){
        return p;
    }


    public static ACodaType getEnum(String type){
        for(ACodaType dt: ACodaType.values()){
            if (dt.name().equalsIgnoreCase(type.trim())) return dt;
        }
        return null;
    }

}
