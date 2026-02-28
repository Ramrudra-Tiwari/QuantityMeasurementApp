package quantitymeasurementapp;

public class QuantityWeight {
       private double value;
       private WeightUnit unit;
       
       public QuantityWeight() {}
       
       public QuantityWeight(Double value,WeightUnit unit) {
    	   if(Double.isNaN(value)||value == null||unit==null) {
    		   throw new IllegalArgumentException("Invalid Input");
    	   }
    	   this.value = value;
    	   this.unit = unit;
       }
       
       public boolean compare(QuantityWeight weightUnit) {
      	 if(weightUnit==null)return false;
      	 
      	  return Math.abs(this.unit.convertToBaseUnit(getValue())-weightUnit.getUnit().convertToBaseUnit(weightUnit.getValue())) < 0.0001;
  	}
       
      public double getValue() {
		return value;
	}

	   public WeightUnit getUnit() {
		   return unit;
	   }

	  @Override
    public boolean equals(Object obj) {
       if(this==obj) return true;
       
       if(obj==null||this.getClass()!=obj.getClass()) return false;
       
       return this.compare((QuantityWeight)obj);}
    
     public QuantityWeight convertTo(WeightUnit targetUnit) {
    	 
    	 double temp = unit.convertToBaseUnit(value)/targetUnit.getConversionFactor();
    	 
    	 return new QuantityWeight(temp,targetUnit);
     }
     
     private double convertBaseToTarget(double val,WeightUnit targetUnit) {
    	 double temp = this.unit.convertToBaseUnit(val)/targetUnit.getConversionFactor();
    	 return temp;
     }
     
     public QuantityWeight add(QuantityWeight val) {
    	 if(val==null)  throw new IllegalArgumentException("Invalid Input");
    	 
    	 QuantityWeight temp = val.convertTo(this.unit);
    	 
    	 return new QuantityWeight(this.value+temp.value,this.unit);
     }
      
     private QuantityWeight addAndConvert(QuantityWeight val,WeightUnit targetUnit) {
    	 if(val==null||targetUnit==null) throw new IllegalArgumentException("Invalid Input");
    	 
    	 double temp1 = convertBaseToTarget(value, targetUnit);
    	 double temp2 = val.convertBaseToTarget(val.getValue(), targetUnit);
    	 
    	 return new QuantityWeight(temp1+temp2,targetUnit);
     }
     public QuantityWeight add(QuantityWeight val,WeightUnit targetUnit) {
    	 return addAndConvert(val, targetUnit);
     }

	 @Override
	 public String toString() {
		return "QuantityWeight [value=" + value + ", unit=" + unit + "]";
	 }
     
}
