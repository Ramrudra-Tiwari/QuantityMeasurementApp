package com.app.quantitymeasurementapp.model;

import java.util.Objects;

import com.app.quantitymeasurementapp.dto.QuantityDTO;

import java.time.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quantity_measurement_entity", indexes = {
@Index(name = "idx_operation", columnList = "operation"),
@Index(name = "idx_measurement_type", columnList = "this_measurement_type"),
@Index(name = "idx_created_at", columnList = "created_at")

})
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityMeasurementEntity implements java.io.Serializable {
	

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@Column(name="this_value",nullable = false)
	private double thisValue;
	
	@Column(name="this_unit", nullable = false)
	private String thisUnit;
	
	@Column(name= "this_measurement_type",nullable = false)
	private String thisMeasurementType;
	
	@Column(name = "that_value",nullable = false)
	private double thatValue;
	
	@Column(name = "that_unit" ,nullable =  false)
	private String thatUnit;
	
	@Column(name = "that_measurement_type",nullable = false)
	private String thatMeasurementType;
	
	// e.g., "COMPARE", "CONVER", "ADD", "SUBTRACT", "DEVIDE"
	
	@Column(name = "operation", nullable = false)
	public String operation;
	
	@Column(name ="result_value")
	public double resultValue;
	
	@Column(name = "result_unit")
	public String resultUnit;
	
	@Column(name = "result_measurement_type")
	public String resultMeasurementType;
	
	// For comparison results like "Equal" or "Not Equal"
	@Column(name = "result_string")
	public String resultString;
	
	// Flag to indicate if an error occurred during the operation
	@Column(name = "is_error")
	public boolean isError;
	
	// For capturing any error messages during operations
	@Column(name = "error_message")
	public String errorMessage;
	
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	

		@PrePersist
		protected void onCreate() { 
			createdAt = LocalDateTime.now();
		}

		@PreUpdate
		protected void onUpdate() {
		updatedAt = LocalDateTime.now();}
		
		public QuantityMeasurementEntity(QuantityDTO thisQuantity,QuantityDTO  thatQuantity,String operation, String result) {
                this(thisQuantity, thatQuantity, operation);
				this.resultString = result;
		}
		
		public QuantityMeasurementEntity(QuantityDTO  thisQuantity,QuantityDTO  thatQuantity,String operation,QuantityDTO  result) {
				this(thisQuantity, thatQuantity, operation);
				this. resultValue = result.getValue();
				this.resultUnit = result.getUnit();
				this. resultMeasurementType = result.getMeasurementType();}
		
		public QuantityMeasurementEntity(QuantityDTO  thisQuantity,QuantityDTO  thatQuantity,String operation, String errorMessage, boolean isError) {
				this(thisQuantity, thatQuantity, operation);
				this.errorMessage = errorMessage;
				this.isError = isError;
		}
		public QuantityMeasurementEntity(QuantityDTO  thisQuantity,QuantityDTO  thatQuantity, String operation) {
			this.thisValue = thisQuantity.getValue();
			this.thisUnit =  thisQuantity.getUnit().toString();
			this.thisMeasurementType = thisQuantity.getMeasurementType();
			this.thatValue = thatQuantity.getValue();
			this.thatUnit = thatQuantity.getUnit().toString();
			this.thatMeasurementType = thatQuantity.getMeasurementType();
			this.operation = operation;
		}
		@Override
	    public boolean equals(Object obj) {

	        if (this == obj)
	            return true;

	        if (obj == null || getClass() != obj.getClass())
	            return false;

	        QuantityMeasurementEntity that = (QuantityMeasurementEntity) obj;
	        return Double.compare(that.thisValue, thisValue) == 0 &&
	                Double.compare(that.thatValue, thatValue) == 0 &&
	                Double.compare(that.resultValue, resultValue) == 0 &&
	                isError == that.isError &&
	                Objects.equals(thisUnit, that.thisUnit) &&
	                Objects.equals(thisMeasurementType, that.thisMeasurementType) &&
	                Objects.equals(thatUnit, that.thatUnit) &&
	                Objects.equals(thatMeasurementType, that.thatMeasurementType) &&
	                Objects.equals(operation, that.operation) &&
	                Objects.equals(resultUnit, that.resultUnit) &&
	                Objects.equals(resultMeasurementType, that.resultMeasurementType) &&
	                Objects.equals(resultString, that.resultString) &&
	                Objects.equals(errorMessage, that.errorMessage);
	    }
        
		
		@Override
		public String toString() {
			return "QuantityMeasurementEntity [thisValue=" + thisValue + ", thisUnit=" + thisUnit
					+ ", thisMeasurementType=" + thisMeasurementType + ", thatValue=" + thatValue + ", thatUnit="
					+ thatUnit + ", thatMeasurementType=" + thatMeasurementType + ", operation=" + operation
					+ ", resultValue=" + resultValue + ", resultUnit=" + resultUnit + ", resultMeasurementType="
					+ resultMeasurementType + ", resultString=" + resultString + ", isError=" + isError
					+ ", errorMessage=" + errorMessage + "]";
		}
		
		
}