package com.example.project.services;

import com.example.project.dto.LogDefectDto;
import com.example.project.model.Defect;
import com.example.project.model.Location;
import com.example.project.model.Vehicle;
import com.example.project.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogDefectsService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public String logDefects(LogDefectDto logDefectDto) {

        //ObjectMapper objectMapper = new ObjectMapper();
        //Vehicle vehicle = objectMapper.convertValue(logDefectDto, Vehicle.class);

        Vehicle vehicle = new Vehicle();

        List<Defect> defectList = defectDto2Defect(vehicle, logDefectDto);

        vehicle.setVehicleNo(logDefectDto.getVehicleNo());
        vehicle.setDefectList(defectList);

        vehicleRepository.save(vehicle);

        return "Defect logged";
    }

    // returns a List<Defect> populated with locations given in a LogDefectDto object
    private List<Defect> defectDto2Defect(Vehicle vehicle, LogDefectDto logDefectDto) {

        List<Defect> defectsList = new ArrayList<>();

        // defectDto has a defect name and locations specified with that defect. Such as ["A", (100, 200), (500, 600)]
        for(LogDefectDto.DefectDto defectDto: logDefectDto.getDefectList()) {

            List<Location> locationList = new ArrayList<>();

            Defect defect = new Defect(defectDto.getDefectName(),locationList, vehicle);

            // for the specified defect gives the locations by one by
            for(LogDefectDto.LocationDto locationDto: defectDto.getLocationList()) {

                Location location = new Location(defect, locationDto.getLocation());
                locationList.add(location);
            }

            defect.setDefectName(defectDto.getDefectName());
            defect.setLocationList(locationList);
            defectsList.add(defect);
        }
        return defectsList;
    }
}