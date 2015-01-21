package org.jlab.evio.clas12;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.coda.jevio.ByteDataTransformer;
import org.jlab.coda.jevio.DataType;
import org.jlab.coda.jevio.EventBuilder;
import org.jlab.coda.jevio.EvioBank;
import org.jlab.coda.jevio.EvioCompactStructureHandler;
import org.jlab.coda.jevio.EvioEvent;
import org.jlab.coda.jevio.EvioException;
import org.jlab.coda.jevio.EvioNode;
import org.jlab.data.io.DataBank;
import org.jlab.data.io.DataDescriptor;
import org.jlab.data.io.DataDictionary;
import org.jlab.data.io.DataEntryType;
import org.jlab.data.io.DataEvent;
import org.jlab.utils.TablePrintout;

public class EvioDataEvent implements DataEvent {
    
    private HashMap<String,String> eventProperties = new HashMap<String,String>();
    private ByteBuffer evioBuffer;
    private EvioCompactStructureHandler structure = null;
    private EvioDataDictionary dictionary = null;
    
    public EvioDataEvent(byte[] buffer, ByteOrder b_order){
        evioBuffer = ByteBuffer.wrap(buffer);
        evioBuffer.order(b_order);
        try {
            structure = new EvioCompactStructureHandler(evioBuffer,DataType.BANK);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public EvioDataEvent(ByteBuffer buff){
        evioBuffer = buff;
        try {
            structure = new EvioCompactStructureHandler(evioBuffer,DataType.BANK);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public EvioDataEvent(ByteBuffer buff,EvioDataDictionary dict){
        evioBuffer = buff;
        dictionary = dict;
        try {
            structure = new EvioCompactStructureHandler(evioBuffer,DataType.BANK);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ByteOrder getByteOrder(){
        return structure.getByteBuffer().order();
    }
    
   public EvioDataEvent(byte[] buffer, ByteOrder b_order, EvioDataDictionary dict){
        evioBuffer = ByteBuffer.wrap(buffer);
        evioBuffer.order(b_order);
        try {
            structure = new EvioCompactStructureHandler(evioBuffer,DataType.BANK);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        dictionary = dict;
        this.setProperty("banks", "*");
        this.setProperty("variables", "*");
    }
    
    @Override
    public String[] getBankList() {
        try {
            // TODO Auto-generated method stub
            List<EvioNode> nodes   = structure.getNodes();
            ArrayList<String> list = new ArrayList<String>();
            String[] descList = dictionary.getDescriptorList();
            for(EvioNode item : nodes){
                //System.out.println(" TAG-NUM = " +  item.getTag() + "   " + item.getNum()
                //+ "  " + item.getDataTypeObj());
                if(item.getDataTypeObj()==DataType.ALSOBANK&&item.getNum()==0){
                    for(String di : descList){
                        if(Integer.parseInt(dictionary.getDescriptor(di).getPropertyString("container_tag"))
                                ==item.getTag()){
                            list.add(di);
                        }
                        //list.add("[" + item.getTag() + ":" + item.getNum() + "]");
                    }
                    // ((EvioDataBank ) bank).addDoubleBuffer(tag, item.);
                }
            }
            String[] banks = new String[list.size()];
            for(int loop = 0; loop < list.size();loop++) banks[loop] = list.get(loop);
            return banks;
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public EvioCompactStructureHandler getStructureHandler(){
        return structure;
    }
    
    public void initEvent(ByteBuffer buffer){
        evioBuffer = buffer;
    }
    
    @Override
    public String[] getColumnList(String bank_name) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public DataDictionary getDictionary() {
        return this.dictionary;
    }
    
    private int[] getTagNum(String path) {
        String[] split_path = path.split("[.]+");
        String bank = split_path[0];
        String col = split_path[1];
        
        DataDescriptor desc = this.dictionary.getDescriptor(bank);
        int tag = desc.getProperty("tag",col);
        int num = desc.getProperty("num",col);
        int[] ret = {tag,num};
        return ret;
    }
    
    @Override
    public float[] getFloat(String path) {
        if(path.contains("/")==true){
            String[] tokens = path.split("/");            
            return this.getFloat(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
        } else {
            int[] tagnum = this.getTagNum(path);
            return this.getFloat(tagnum[0],tagnum[1]);
        }
        //int[] tagnum = this.getTagNum(path);
        //return this.getFloat(tagnum[0],tagnum[1]);
    }
    
    public float[] getFloat(int tag, int num) {
         EvioNode node = this.getNodeFromTree(tag, num, DataType.FLOAT32);
        if(node!=null){
             try {
                 ByteBuffer buffer = structure.getData(node);
                 float[]  nodedata = ByteDataTransformer.toFloatArray(buffer);
                 return nodedata;
             } catch (EvioException ex) {
                 Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
        return null;
    }
    
    @Override
    public void setFloat(String path, float[] arr) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void appendFloat(String path, float[] arr) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public int[] getInt(String path) {
        if(path.contains("/")==true){
            String[] tokens = path.split("/");            
            return this.getInt(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
        } else {
            int[] tagnum = this.getTagNum(path);
            return this.getInt(tagnum[0],tagnum[1]);
        }
    }
    
    public int[] getInt(int tag, int num) {
        EvioNode node = this.getNodeFromTree(tag, num, DataType.INT32);
        if(node!=null){
            try {
                ByteBuffer buffer = structure.getData(node);
                int[] nodedata = ByteDataTransformer.toIntArray(buffer);
                return nodedata;
            } catch (EvioException ex) {
                Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    @Override
    public void setInt(String path, int[] arr) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void appendInt(String path, int[] arr) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public short[] getShort(String path) {
        int[] tagnum = this.getTagNum(path);
        return this.getShort(tagnum[0],tagnum[1]);
    }
	
    public short[] getShort(int tag, int num) {
        return null;
    }
    
    @Override
    public void setShort(String path, short[] arr) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void appendShort(String path, short[] arr) {
        // TODO Auto-generated method stub
        
    }    
    
    public byte[] getComposite(int tag, int num){
        EvioNode node = this.getNodeFromTree(tag, num, DataType.COMPOSITE);
        if(node!=null){
            try {
                ByteBuffer buffer = structure.getData(node);
                byte[] nodedata = ByteDataTransformer.toByteArray(buffer);
                return nodedata;
            } catch (EvioException ex) {
                Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;        
    }
    
    
    public boolean hasBank(int tag, int num){
        EvioNode node = this.getNodeFromTree(tag, num, DataType.COMPOSITE);
        if(node==null) return false;
        return true;
    }
    
    @Override
    public boolean hasBank(String bank_name) {
        
        if(this.dictionary.getDescriptor(bank_name)==null){
            System.err.println("[EvioDataEvent::hasBank] ( ERROR ) ---> " + 
                    " there is no descriptor with name " + bank_name);
            return false;
        }
        
        EvioDataDescriptor desc = (EvioDataDescriptor) this.dictionary.getDescriptor(bank_name);
        if(desc==null) return false;
        int nodetag = Integer.parseInt(desc.getPropertyString("container_tag"));
        EvioNode banknode = this.getNodeFromTree(nodetag, 0, DataType.ALSOBANK);
        if(banknode==null) return false;
        return true;
    }
    
    @Override
    public DataBank getBank(String bank_name) {
        EvioDataDescriptor desc = (EvioDataDescriptor) this.dictionary.getDescriptor(bank_name);
        if(desc==null) return null;
        EvioDataBank bank = new EvioDataBank(desc);
        String[] entries = desc.getEntryList();

        for(String item : entries){
            //System.err.println("entry = " + item);
            //if(item.getValue()<20){
            int type = desc.getProperty("type", item);
            if(DataEntryType.getType(type)==DataEntryType.INTEGER){
                int[] data = this.getInt(bank_name+"."+item);
                if(data!=null){
                    bank.setInt(item, data);
                } else {
                    bank.setInt(item, new int[0]);
                }
            }
            if(DataEntryType.getType(type)==DataEntryType.DOUBLE){
                
                double[] data = this.getDouble(bank_name+"."+item);
                if(data!=null){
                    bank.setDouble(item, data);
                } else {
                    bank.setDouble(item, new double[0]);
                }                
            }
            
            if(DataEntryType.getType(type)==DataEntryType.FLOAT){
                
                float[] data = this.getFloat(bank_name+"."+item);
                if(data!=null){
                    bank.setFloat(item, data);
                } else {
                    bank.setFloat(item, new float[0]);
                }                
            }
            if(DataEntryType.getType(type)==DataEntryType.SHORT){
                
                short[] data = this.getShort(bank_name+"."+item);
                if(data!=null){
                    bank.setShort(item, data);
                } else {
                    bank.setShort(item, new short[0]);
                }                
            }
            
            if(DataEntryType.getType(type)==DataEntryType.BYTE){
                
                byte[] data = this.getByte(bank_name+"."+item);
                if(data!=null){
                    bank.setByte(item, data);
                } else {
                    bank.setByte(item, new byte[0]);
                }                
            }
            //} else {
                
            //}
            //System.out.println(item.getKey() + " " + item.getValue() 
            //        + "  " + desc.types.get(item.getKey()));
        }
        
        return bank;
    }

    @Override
    public void getBank(String bank_name, DataBank bank) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void show(){
        
        //System.out.println("-----> event show");
        //dictionary.show();
        String[] bankList = this.getBankList();
        TablePrintout table = new TablePrintout("bank:nrows:ncols","24:12:12");
        
        if(bankList!=null){
            for(String bank : bankList){
                String[] tokens = new String[3];
                tokens[0] = bank;
                DataBank dbank = this.getBank(bank);
                Integer ncols  = dbank.columns();
                Integer nrows  = dbank.rows();
                tokens[1] = nrows.toString(); 
                tokens[2] = ncols.toString();
                table.addData(tokens);
                //System.out.println("BANK [] ---> " + bank);
            }
        }
        table.show();
    }
    
    public EvioNode getNodeFromTree(int parent_tag, int tag, int num, DataType type){
        try {
            List<EvioNode>  nodes = structure.getChildNodes();
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public EvioNode getNodeFromTree(int tag, int num, DataType type){
        try {
            List<EvioNode> nodes   = structure.getNodes();
            if(nodes==null) return null;
            for(EvioNode item: nodes){
                if(type==DataType.INT32){
                    if(item.getTag()==tag&&item.getNum()==num&&
                            (item.getDataTypeObj()==DataType.INT32||item.getDataTypeObj()==DataType.UINT32))
                        return item;
                } else {
                    if(item.getTag()==tag&&item.getNum()==num&&
                            item.getDataTypeObj()==type)
                        return item;
                }
                /*
                if(item.getTag()==tag&&item.getNum()==num&&
                item.getDataTypeObj()==type)
                return item;*/
            }            
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public double[] getDouble(int tag, int num){
        EvioNode node = this.getNodeFromTree(tag,num,DataType.DOUBLE64);
        if(node!=null){
            try {
                ByteBuffer buffer = structure.getData(node);
                double[] nodedata = ByteDataTransformer.toDoubleArray(buffer);
                return nodedata;
            } catch (EvioException ex) {
                Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //double[] ret = {0.0};
        return null;
    }
    
    @Override
    public double[] getDouble(String path) {
        if(path.contains("/")==true){
            String[] tokens = path.split("/");
            return this.getDouble(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
        } else {
            int[] tagnum = this.getTagNum(path);
            return this.getDouble(tagnum[0],tagnum[1]);
        }
    }

    @Override
    public void setDouble(String path, double[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendDouble(String path, double[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    @Override
    public void appendBank(DataBank bank) {
        //System.err.println("---------> 1");
        String parent_tag = bank.getDescriptor().getPropertyString("parent_tag");
        String container_tag = bank.getDescriptor().getPropertyString("container_tag");
        //System.err.println("---------> 2");
        EvioEvent baseBank = new EvioEvent(Integer.parseInt(parent_tag), DataType.ALSOBANK, 0);
        //System.err.println("---------> 3");
        EvioBank sectionBank = new EvioBank(Integer.parseInt(container_tag), DataType.ALSOBANK, 0);
        //System.err.println("---------> 4");

        EventBuilder builder = new EventBuilder(baseBank);
        ByteOrder byteOrder = structure.getByteBuffer().order();
        
        baseBank.setByteOrder(byteOrder);
        sectionBank.setByteOrder(byteOrder);
        //doubleBank.setByteOrder(byteOrder);
        //System.err.println("------------ adding bank ");
        //System.err.println("------------ adding bank " + bank.getDescriptor().getName());
        try {
            String[] entries = bank.getDescriptor().getEntryList();
            for(String entry : entries){
                //System.out.println("----> adding entry " + entry);
                int e_tag = bank.getDescriptor().getProperty("tag", entry);
                int e_num = bank.getDescriptor().getProperty("num", entry);
                int e_typ = bank.getDescriptor().getProperty("type", entry);
                if(DataEntryType.getType(e_typ)==DataEntryType.INTEGER){
                    EvioBank dataBank = new EvioBank(e_tag, DataType.INT32, e_num);
                    dataBank.setByteOrder(byteOrder);
                    dataBank.appendIntData(bank.getInt(entry));
                    builder.addChild(sectionBank, dataBank);
                }
                
                if(DataEntryType.getType(e_typ)==DataEntryType.DOUBLE){
                    EvioBank dataBank = new EvioBank(e_tag, DataType.DOUBLE64, e_num);
                    dataBank.setByteOrder(byteOrder);
                    dataBank.appendDoubleData(bank.getDouble(entry));
                    builder.addChild(sectionBank, dataBank);
                }
                
                if(DataEntryType.getType(e_typ)==DataEntryType.FLOAT){
                    EvioBank dataBank = new EvioBank(e_tag, DataType.FLOAT32, e_num);
                    dataBank.setByteOrder(byteOrder);
                    dataBank.appendFloatData(bank.getFloat(entry));
                    builder.addChild(sectionBank, dataBank);
                }
                
                if(DataEntryType.getType(e_typ)==DataEntryType.SHORT){
                    EvioBank dataBank = new EvioBank(e_tag, DataType.SHORT16, e_num);
                    dataBank.setByteOrder(byteOrder);
                    dataBank.appendShortData(bank.getShort(entry));
                    builder.addChild(sectionBank, dataBank);
                }
                
                if(DataEntryType.getType(e_typ)==DataEntryType.BYTE){
                    EvioBank dataBank = new EvioBank(e_tag, DataType.CHAR8, e_num);
                    dataBank.setByteOrder(byteOrder);
                    dataBank.appendByteData(bank.getByte(entry));
                    builder.addChild(sectionBank, dataBank);
                }
            }
            
            builder.addChild(baseBank, sectionBank);
            
            int byteSize = baseBank.getTotalBytes();
            ByteBuffer bb = ByteBuffer.allocate(byteSize);
            //System.out.println("-------> adding bank " + bank.getDescriptor().getName()
            //+ "  size = " + byteSize);
            bb.order(byteOrder);
            baseBank.write(bb);
            bb.flip();
            //System.out.println("-----> prior size = " + structure.getByteBuffer().limit());
            ByteBuffer newBuffer = structure.addStructure(bb);
            //System.out.println("---> new byte buffer has size " + newBuffer.limit()            
            //        +  "   changed from " + structure.getByteBuffer().limit());
            //structure.
            EvioCompactStructureHandler handler = new EvioCompactStructureHandler(structure.getByteBuffer(),DataType.BANK);
            structure = handler;
            /*
            for (Map.Entry<String, int[]> bank : integerContainer.entrySet()) {
            EvioBank dataBank = new EvioBank(tag, DataType.INT32, bank.getKey());
            dataBank.setByteOrder(byteOrder);
            dataBank.appendIntData(bank.getValue());
            builder.addChild(intBank, dataBank);
            }
            
            for (Entry<Integer, double[]> bank : doubleBanks.entrySet()) {
            EvioBank dataBank = new EvioBank(tag, DataType.DOUBLE64, bank.getKey());
            dataBank.setByteOrder(byteOrder);
            dataBank.appendDoubleData(bank.getValue());
            builder.addChild(doubleBank, dataBank);
            }*/
        } catch (EvioException e) {
            e.printStackTrace();
        }
        
    }        

    @Override
    public ByteBuffer getEventBuffer() {
        return structure.getByteBuffer();
    }

    @Override
    public void setProperty(String property, String value) {
        if(eventProperties.containsKey(property)==true){
            eventProperties.remove(property);
        }         
        eventProperties.put(property, value);        
    }

    @Override
    public String getProperty(String property) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public byte[] getByte(int tag, int num){
        EvioNode node = this.getNodeFromTree(tag,num,DataType.CHAR8);
        if(node!=null){
            try {
                ByteBuffer buffer = structure.getData(node);
                byte[] nodedata = ByteDataTransformer.toByteArray(buffer);
                return nodedata;
            } catch (EvioException ex) {
                Logger.getLogger(EvioDataEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //byte[] ret = {0};
        return null;
    }
    @Override
    public byte[] getByte(String path) {
        if(path.contains("/")==true){
            String[] tokens = path.split("/");
            return this.getByte(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
        } else {
            int[] tagnum = this.getTagNum(path);
            return this.getByte(tagnum[0],tagnum[1]);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setByte(String path, byte[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendByte(String path, byte[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendBanks(DataBank... banklist) {
        
        String  common_tag = banklist[0].getDescriptor().getPropertyString("parent_tag");
        Boolean check = true;
        for(int loop = 0; loop < banklist.length; loop++){
            String btag = banklist[loop].getDescriptor().getPropertyString("parent_tag");
            if(btag.compareTo(common_tag)!=0) check = false;
        }
        
        //System.out.println("Bank consistency check = " + check);

        String parent_tag = common_tag;

        try {
            
            EvioEvent baseBank = new EvioEvent(Integer.parseInt(parent_tag), DataType.ALSOBANK, 0);
            ByteOrder byteOrder = structure.getByteBuffer().order();
            baseBank.setByteOrder(byteOrder);
            EventBuilder builder = new EventBuilder(baseBank);
            
            
            
            for(DataBank bank : banklist){
                String container_tag = bank.getDescriptor().getPropertyString("container_tag");
                //System.err.println("---------> 2");
                
                //System.err.println("---------> 3");
                EvioBank sectionBank = new EvioBank(Integer.parseInt(container_tag), DataType.ALSOBANK, 0);
                //System.err.println("---------> 4");
                
                sectionBank.setByteOrder(byteOrder);
                //doubleBank.setByteOrder(byteOrder);
                //System.err.println("------------ adding bank ");
                //System.err.println("------------ adding bank " + bank.getDescriptor().getName());
                
                
                String[] entries = bank.getDescriptor().getEntryList();
                for(String entry : entries){
                    //System.out.println("----> adding entry " + entry);
                    int e_tag = bank.getDescriptor().getProperty("tag", entry);
                    int e_num = bank.getDescriptor().getProperty("num", entry);
                    int e_typ = bank.getDescriptor().getProperty("type", entry);
                    if(DataEntryType.getType(e_typ)==DataEntryType.INTEGER){
                        EvioBank dataBank = new EvioBank(e_tag, DataType.INT32, e_num);
                        dataBank.setByteOrder(byteOrder);
                        dataBank.appendIntData(bank.getInt(entry));
                        builder.addChild(sectionBank, dataBank);
                    }
                    
                    if(DataEntryType.getType(e_typ)==DataEntryType.DOUBLE){
                        EvioBank dataBank = new EvioBank(e_tag, DataType.DOUBLE64, e_num);
                        dataBank.setByteOrder(byteOrder);
                        dataBank.appendDoubleData(bank.getDouble(entry));
                        builder.addChild(sectionBank, dataBank);
                    }
                    
                    if(DataEntryType.getType(e_typ)==DataEntryType.FLOAT){
                        EvioBank dataBank = new EvioBank(e_tag, DataType.FLOAT32, e_num);
                        dataBank.setByteOrder(byteOrder);
                        dataBank.appendFloatData(bank.getFloat(entry));
                        builder.addChild(sectionBank, dataBank);
                    }
                    
                    if(DataEntryType.getType(e_typ)==DataEntryType.SHORT){
                        EvioBank dataBank = new EvioBank(e_tag, DataType.SHORT16, e_num);
                        dataBank.setByteOrder(byteOrder);
                        dataBank.appendShortData(bank.getShort(entry));
                        builder.addChild(sectionBank, dataBank);
                    }
                    
                    if(DataEntryType.getType(e_typ)==DataEntryType.BYTE){
                        EvioBank dataBank = new EvioBank(e_tag, DataType.CHAR8, e_num);
                        dataBank.setByteOrder(byteOrder);
                        dataBank.appendByteData(bank.getByte(entry));
                        builder.addChild(sectionBank, dataBank);
                    }
                }
                
                builder.addChild(baseBank, sectionBank);
            }
            
            int byteSize = baseBank.getTotalBytes();
            ByteBuffer bb = ByteBuffer.allocate(byteSize);
                //System.out.println("-------> adding bank " + bank.getDescriptor().getName()
            //+ "  size = " + byteSize);
            bb.order(byteOrder);
            baseBank.write(bb);
            bb.flip();
            //System.out.println("-----> prior size = " + structure.getByteBuffer().limit());
            ByteBuffer newBuffer = structure.addStructure(bb);
            //System.out.println("---> new byte buffer has size " + newBuffer.limit()            
            //        +  "   changed from " + structure.getByteBuffer().limit());
            //structure.
            EvioCompactStructureHandler handler = new EvioCompactStructureHandler(structure.getByteBuffer(),DataType.BANK);
            structure = handler;
            /*
            for (Map.Entry<String, int[]> bank : integerContainer.entrySet()) {
            EvioBank dataBank = new EvioBank(tag, DataType.INT32, bank.getKey());
            dataBank.setByteOrder(byteOrder);
            dataBank.appendIntData(bank.getValue());
            builder.addChild(intBank, dataBank);
            }
            
            for (Entry<Integer, double[]> bank : doubleBanks.entrySet()) {
            EvioBank dataBank = new EvioBank(tag, DataType.DOUBLE64, bank.getKey());
            dataBank.setByteOrder(byteOrder);
            dataBank.appendDoubleData(bank.getValue());
            builder.addChild(doubleBank, dataBank);
            }*/
        } catch (EvioException e) {
            e.printStackTrace();
        }
    }
}
