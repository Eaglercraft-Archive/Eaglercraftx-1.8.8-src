
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 9

> INSERT  8 : 10  @  8

+ 	public ModelBakery modelbakerytmp; // eagler hack
+ 

> CHANGE  6 : 14  @  6 : 10

~ 		modelbakerytmp = new ModelBakery(iresourcemanager, this.texMap, this.modelProvider);
~ 		try {
~ 			this.modelRegistry = modelbakerytmp.setupModelRegistry();
~ 			this.defaultModel = (IBakedModel) this.modelRegistry.getObject(ModelBakery.MODEL_MISSING);
~ 			this.modelProvider.reloadModels();
~ 		} finally {
~ 			modelbakerytmp = null;
~ 		}

> EOF
