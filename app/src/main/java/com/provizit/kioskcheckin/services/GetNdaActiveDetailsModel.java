package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.NdaActiveDetails;

import java.io.Serializable;

public class GetNdaActiveDetailsModel implements Serializable {
   private Integer result;
   private Float total_counts;
   private Boolean incomplete_data;
   public NdaActiveDetails items;

   public Integer getResult() {
      return result;
   }

   public void setResult(Integer result) {
      this.result = result;
   }

   public Float getTotal_counts() {
      return total_counts;
   }

   public void setTotal_counts(Float total_counts) {
      this.total_counts = total_counts;
   }

   public Boolean getIncomplete_data() {
      return incomplete_data;
   }

   public void setIncomplete_data(Boolean incomplete_data) {
      this.incomplete_data = incomplete_data;
   }

   public NdaActiveDetails getItems() {
      return items;
   }

   public void setItems(NdaActiveDetails items) {
      this.items = items;
   }




}
