package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.*;
import de.davelee.trams.operations.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the VehicleService class and ensures that it works successfully. Mocks are used for the database layer.
 * @author Dave Lee
 */
@SpringBootTest
public class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    /**
     * Ensure that a vehicle can be added successfully to the mock database.
     */
    @Test
    public void testAddVehicle() {
        //Test tram
        Vehicle vehicle = Vehicle.builder()
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.DELIVERED)
                .fleetNumber("213")
                .company("Lee Buses")
                .vehicleType(VehicleType.TRAM)
                .typeSpecificInfos(Map.of("bidirectional", "true"))
                .build();
        Mockito.when(vehicleRepository.insert(vehicle)).thenReturn(vehicle);
        assertTrue(vehicleService.addVehicle(vehicle));
    }

    /**
     * Ensure that data can be retrieved from the mock database and supplied as a response.
     */
    @Test
    public void testRetrieveByCompany() {
        //Test data.
        Vehicle tram = Vehicle.builder()
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.now().minusYears(10))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.DELIVERED)
                .fleetNumber("213")
                .company("Lee Buses")
                .typeSpecificInfos(Map.of("Bidirectional", "true"))
                .vehicleType(VehicleType.TRAM)
                .build();
        Mockito.when(vehicleRepository.findByCompany("Lee Buses")).thenReturn(List.of(tram));
        //Now do actual test.
        List<Vehicle> vehicles = vehicleService.retrieveVehiclesByCompany("Lee Buses");
        assertEquals(VehicleType.TRAM, vehicles.get(0).getVehicleType());
    }

    /**
     * Ensure that data can be retrieved by searching for fleet number and company name
     * from the mock database and supplied as a response.
     */
    @Test
    public void testRetrieveVehiclesByCompanyAndFleetNumber() {
        //Test data.
        Vehicle vehicle = Vehicle.builder()
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.now().minusDays(7))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.DELIVERED)
                .fleetNumber("213")
                .company("Lee Buses")
                .typeSpecificInfos(Map.of("Bidirectional", "true"))
                .vehicleType(VehicleType.TRAM)
                .build();
        Mockito.when(vehicleRepository.findByCompanyAndFleetNumberStartsWith("Lee", "21")).thenReturn(List.of(vehicle));
        //Now do actual test.
        List<Vehicle> vehicles = vehicleService.retrieveVehiclesByCompanyAndFleetNumber("Lee", "21");
        assertEquals(VehicleType.TRAM, vehicles.get(0).getVehicleType());
    }

    /**
     * Ensure that data can be retrieved by searching for fleet number
     * from the mock database and supplied as a response.
     */
    @Test
    public void testRetrieveVehiclesByFleetNumber() {
        //Test data.
        Vehicle tram = Vehicle.builder()
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.now().minusDays(7))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.DELIVERED)
                .fleetNumber("213")
                .company("Lee Buses")
                .typeSpecificInfos(Map.of("Bidirectional", "true"))
                .vehicleType(VehicleType.TRAM)
                .build();
        tram.addVehicleHistoryEntry(LocalDate.of(2021,3,1), VehicleHistoryReason.PURCHASED, "Purchased!" );
        tram.addVehicleHistoryEntry(LocalDate.of(2021,3,25), VehicleHistoryReason.DELIVERED, "Delivered!");
        tram.addVehicleHistoryEntry(LocalDate.of(2021,4,1), VehicleHistoryReason.INSPECTED, "Inspected!");
        tram.addVehicleHistoryEntry(LocalDate.of(2021,10,1), VehicleHistoryReason.SOLD, "Sold!");
        Mockito.when(vehicleRepository.findByCompanyAndFleetNumberStartsWith("Lee Buses", "21")).thenReturn(List.of(tram));
        //Now do actual test.
        List<Vehicle> vehicleResponseList = vehicleService.retrieveVehiclesByCompanyAndFleetNumber("Lee Buses", "21");
        assertEquals(VehicleType.TRAM, vehicleResponseList.get(0).getVehicleType());
        assertEquals(vehicleResponseList.get(0).getVehicleHistoryEntryList().get(0).getVehicleHistoryReason().getText(), "Purchased");
        assertEquals(vehicleResponseList.get(0).getVehicleHistoryEntryList().get(0).getDate(), LocalDate.of(2021,3,1));
        assertEquals(vehicleResponseList.get(0).getVehicleHistoryEntryList().get(0).getComment(), "Purchased!");
        assertNull(vehicleResponseList.get(0).getVehicleHistoryEntryList().get(0).getId());
        assertEquals(vehicleResponseList.get(0).getVehicleHistoryEntryList().get(1).getVehicleHistoryReason().getText(), "Delivered");
        assertEquals(vehicleResponseList.get(0).getVehicleHistoryEntryList().get(2).getVehicleHistoryReason().getText(), "Inspected");
        assertEquals(vehicleResponseList.get(0).getVehicleHistoryEntryList().get(3).getVehicleHistoryReason().getText(), "Sold");
    }

    /**
     * Test case: add timesheet hours.
     * Expected result: true.
     */
    @Test
    public void testAddHoursForDate() {
        //Test data
        Vehicle vehicle = Vehicle.builder()
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.now().minusDays(7))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.DELIVERED)
                .fleetNumber("213")
                .company("Lee Buses")
                .typeSpecificInfos(Map.of("Bidirectional", "true"))
                .vehicleType(VehicleType.TRAM)
                .build();
        //Mock important method in repository.
        Mockito.when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        //do actual test.
        assertTrue(vehicleService.addHoursForDate(vehicle, 8, LocalDate.of(2020,3,1) ));
        assertTrue(vehicleService.addHoursForDate(vehicle, 1, LocalDate.of(2020,3,1) ));
    }

    /**
     * Test case: add a new history entry.
     * Expected result: true.
     */
    @Test
    public void testAddUserHistoryEntry() {
        //Test data
        Vehicle vehicle = Vehicle.builder()
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.now().minusDays(7))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.DELIVERED)
                .fleetNumber("213")
                .company("Lee Buses")
                .typeSpecificInfos(Map.of("Bidirectional", "true"))
                .vehicleType(VehicleType.TRAM)
                .build();
        //Mock important method in repository.
        Mockito.when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        //do actual test.
        assertTrue(vehicleService.addVehicleHistoryEntry(vehicle, LocalDate.of(2020,3,1), VehicleHistoryReason.PURCHASED, "Welcome to the company!"));
        assertTrue(vehicleService.addVehicleHistoryEntry(vehicle, LocalDate.of(2020,3,31), VehicleHistoryReason.DELIVERED, "Vehicle has been delivered!"));
    }

}
