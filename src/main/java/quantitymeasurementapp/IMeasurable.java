package quantitymeasurementapp;

public interface IMeasurable {
     double getConversionFactor();
     
     double convertToBaseUnit(double value);
     
     double convertFromBaseUnit(double value);
     
     String getUnitName();
     
     SupportArithemetic supportArithemetic = ()-> true;
     
     default boolean supportsArithemetics() {
    	 return supportArithemetic.isSupported();
     }
     
     default void validateOperationSupport(String operation){
    	 if(!supportsArithemetics()) {
    		 throw new UnsupportedOperationException("The Operation doest not supported "+operation);
    	 }
     }
}