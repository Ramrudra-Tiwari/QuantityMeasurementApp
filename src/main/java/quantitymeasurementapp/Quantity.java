package quantitymeasurementapp;


public class Quantity<T extends IMeasurable> {
     private double value;
     private T unit;
     
     public Quantity(Double value,T unit){
    	 if(Double.isNaN(value)||value==null||unit==null) {
    		 throw new IllegalArgumentException("Invalid Input");
    	 }
    	 this.value = value;
    	 this.unit = unit;
     }
     
     public double getValue() {
		return value;
	}

	 public T getUnit() {
		 return unit;
	 }

	 @Override
    public boolean equals(Object obj) {
    	if(obj==this) return true;
    	
    	if(obj==null||this.getClass()!=obj.getClass()) return false;
    	Quantity<T> temp= (Quantity<T>)obj;
    	if(temp.unit.getClass()!=this.unit.getClass()) return false;
    	return this.compare((Quantity<T>)obj);
    }
     
     public boolean compare(Quantity<T> obj) {
    	 if(obj==null) return false;
    	 
    	 return Math.abs(this.unit.convertToBaseUnit(value)-obj.getUnit().convertToBaseUnit(obj.getValue()))<0.0001;
     }
     
     public Quantity<T> convertTo(T unit) {
    	 if(unit==null) throw new IllegalArgumentException("Invalid Input");
    	 double temp = this.unit.convertToBaseUnit(value)/unit.getConversionFactor();
    	 return new Quantity<>(temp, unit);
     }
     public Quantity<T> convertTo(Quantity<T> val){
    	 if(val==null) throw new IllegalArgumentException("Invalid Input");
    	 double temp = this.unit.convertToBaseUnit(value)/val.getUnit().getConversionFactor();
    	 return new Quantity<>(temp, unit);
     }
     private double convertBaseToTargetUnit(Double lengthInInches,T targetUnit) {
    	 if(lengthInInches==null||targetUnit==null) throw new IllegalArgumentException("Invalid Input");
		 return (lengthInInches*this.unit.getConversionFactor())/targetUnit.getConversionFactor();
	 }
     
     public Quantity<T> add(Quantity<T> val){
    	 if(val==null) throw new IllegalArgumentException("Invalid Input");
    	 double temp = val.convertBaseToTargetUnit(val.getValue(), this.unit);
    	 return new Quantity<>(temp+value, this.unit);
     }
     
     private Quantity<T> addAndConvert(Quantity<T> val1, T unit){
    	 if(unit==null||val1==null) throw new IllegalArgumentException("Invalid Input");
    	 double temp1 = val1.convertBaseToTargetUnit(val1.getValue(), unit);
    	 double temp2 = this.convertBaseToTargetUnit(value, unit);
    	 
    	 return new Quantity<>(temp2+temp1, unit);
     }
     public Quantity<T> add(Quantity<T> val1,T unit){
    	 
    	 return this.addAndConvert(val1, unit);
     }

	 @Override
	 public String toString() {
		return "Quantity [value=" + value + ", unit=" + unit + "]";
	 }
     
     
}