package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;

@Component
public class FileItemToRaportoitavaLiite {

	public static RaportoitavaLiite convert(FileItem fileItem) throws IOException {
		RaportoitavaLiite liite = new RaportoitavaLiite();
		
		liite.setLiitetiedostonNimi(fileItem.getName());
		liite.setSisaltotyyppi(fileItem.getContentType());
		if (false) {
		byte[] zippedLiite = zipLiitetiedosto(fileItem.getName(), fileItem.get());
			liite.setLiitetiedosto(zippedLiite);
		} else {
			liite.setLiitetiedosto(fileItem.get());
		}
		liite.setAikaleima(new Date());
		
		return liite;
	}
	
	private static byte[] zipLiitetiedosto(String liitetiedostonNimi, byte[] liitetiedosto) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipStream = new ZipOutputStream(out);
        
        zipStream.putNextEntry(new ZipEntry(liitetiedostonNimi));
        zipStream.write(liitetiedosto);
        zipStream.closeEntry();

        zipStream.close();
        return out.toByteArray();
    }
}
