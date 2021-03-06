package net.adamhilton.handylyst.injection.module;

import net.adamhilton.handylyst.data.local.ListRepo;
import net.adamhilton.handylyst.data.local.RealmListRepo;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DataModule {
    @Binds
    abstract ListRepo bindListRepo(RealmListRepo realmListRepo);
}
