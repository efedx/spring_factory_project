package com.example.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "defects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Embeddable
public class Defect extends Id {

    private String defectName;

    @OneToMany(targetEntity = Location.class, cascade = CascadeType.ALL)
    @JsonManagedReference("defect_location")
    private List<Location> locationList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference("vehicle_defect")
    Vehicle vehicle;

    public Defect(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public Defect(Vehicle vehicle, String defectName) {
        this.vehicle = vehicle;
        this.defectName = defectName;
    }

    public Defect(String defectName, ArrayList<Location> locationList) {
        this.defectName = defectName;
        this.locationList = locationList;
    }
}