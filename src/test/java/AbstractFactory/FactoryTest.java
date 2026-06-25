package AbstractFactory;

import VillageElements.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Abstract Factory family: {@link WorkerFactory}, {@link BuildingFactory},
 * and {@link FactoryProducer}. All "invalid type" exceptions extend {@link ClassNotFoundException}.
 */
class FactoryTest {

    @Test
    void workerFactoryCreatesSoldier() throws Exception {
        AbstractFactory factory = new WorkerFactory();
        assertInstanceOf(Soldier.class, factory.getVillageEntity(WorkerFactory.SOLDIER));
    }

    @Test
    void workerFactoryCreatesArcher() throws Exception {
        assertInstanceOf(Archer.class, new WorkerFactory().getVillageEntity(WorkerFactory.ARCHER));
    }

    @Test
    void workerFactoryIsCaseInsensitive() throws Exception {
        assertInstanceOf(Knight.class, new WorkerFactory().getVillageEntity("knight"));
    }

    @Test
    void workerFactoryRejectsUnknownType() {
        assertThrows(ClassNotFoundException.class,
                () -> new WorkerFactory().getVillageEntity("DRAGON"));
    }

    @Test
    void buildingFactoryCreatesFarm() throws Exception {
        assertInstanceOf(Farm.class, new BuildingFactory().getVillageEntity(BuildingFactory.FARM));
    }

    @Test
    void buildingFactoryCreatesArcherTower() throws Exception {
        assertInstanceOf(ArcherTower.class, new BuildingFactory().getVillageEntity(BuildingFactory.ARCHER_TOWER));
    }

    @Test
    void buildingFactoryRejectsUnknownType() {
        assertThrows(ClassNotFoundException.class,
                () -> new BuildingFactory().getVillageEntity("CASTLE"));
    }

    @Test
    void factoryProducerReturnsRequestedFactories() throws Exception {
        assertInstanceOf(WorkerFactory.class, FactoryProducer.getFactory(FactoryProducer.WORKER));
        assertInstanceOf(BuildingFactory.class, FactoryProducer.getFactory(FactoryProducer.BUILDING));
    }

    @Test
    void factoryProducerRejectsUnknownFactory() {
        assertThrows(ClassNotFoundException.class, () -> FactoryProducer.getFactory("MAGIC"));
    }
}
