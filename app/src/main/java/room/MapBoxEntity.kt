package room

import androidx.room.Entity
import androidx.room.Index

@Entity(indices = [Index(value = ["city"], unique = true)])
class MapBoxEntity