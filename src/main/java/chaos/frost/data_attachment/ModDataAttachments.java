package chaos.frost.data_attachment;

import chaos.frost.NewFrostwalker;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class ModDataAttachments {
    public static final AttachmentType<Boolean> IS_FROSTWALKER_ENABLED = AttachmentRegistry.createDefaulted(
            NewFrostwalker.id("is_frostwalker_enabled"),
            () -> true
    );
}
